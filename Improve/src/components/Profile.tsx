import { useState, useRef } from "react";
import { ArrowLeft, Edit2, Save, LogOut, Camera, User, Heart, Droplet, Briefcase, Shield, Check } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card, CardContent } from "./ui/card";
import { Badge } from "./ui/badge";
import { motion, AnimatePresence } from "motion/react";
import { toast } from "sonner@2.0.3";
import { Toaster } from "./ui/sonner";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { Textarea } from "./ui/textarea";

interface ProfileData {
  name: string;
  age: string;
  bloodGroup: string;
  permanentIllness: string;
  email: string;
  phone: string;
  emergencyContact: string;
  address: string;
  profileImage: string | null;
}

export function Profile({ onBack, onLogout }: { onBack: () => void; onLogout: () => void }) {
  const [isEditing, setIsEditing] = useState(false);
  const [showLogoutDialog, setShowLogoutDialog] = useState(false);
  const [profileData, setProfileData] = useState<ProfileData>({
    name: "John Doe",
    age: "32",
    bloodGroup: "A+",
    permanentIllness: "None",
    email: "john.doe@example.com",
    phone: "+1 234 567 8900",
    emergencyContact: "+1 234 567 8901",
    address: "123 Health Street, Medical City",
    profileImage: null,
  });
  const [tempData, setTempData] = useState<ProfileData>(profileData);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

  const handleEdit = () => {
    setIsEditing(true);
    setTempData(profileData);
  };

  const handleSave = () => {
    setProfileData(tempData);
    setIsEditing(false);
    toast.success("Profile updated successfully!", {
      description: "Your changes have been saved.",
      duration: 3000,
    });
  };

  const handleCancel = () => {
    setTempData(profileData);
    setIsEditing(false);
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setTempData({ ...tempData, profileImage: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleLogout = () => {
    toast.success("Logged out successfully!", {
      duration: 2000,
    });
    setTimeout(() => {
      // Handle logout logic here
      console.log("User logged out");
      onLogout();
    }, 1000);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-purple-50 to-pink-50 pb-24">
      <Toaster />
      
      {/* Header */}
      <header className="sticky top-0 z-40 bg-white/80 backdrop-blur-lg border-b border-gray-200/50 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <Button 
                variant="ghost" 
                size="icon" 
                onClick={onBack}
                className="hover:bg-gray-100 transition-colors"
              >
                <ArrowLeft className="h-5 w-5" />
              </Button>
              <h1 className="text-gray-900">Profile</h1>
            </div>
            <Badge variant="outline" className="bg-gradient-to-r from-blue-500 to-purple-600 text-white border-0">
              Premium
            </Badge>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          {/* Profile Image Section */}
          <div className="mb-6 flex justify-center">
            <motion.div 
              className="relative"
              whileHover={{ scale: 1.05 }}
              transition={{ type: "spring", stiffness: 300 }}
            >
              <div className="relative w-32 h-32 rounded-full bg-gradient-to-br from-blue-400 via-purple-400 to-pink-400 p-1 shadow-lg">
                <div className="w-full h-full rounded-full bg-white flex items-center justify-center overflow-hidden">
                  {(isEditing ? tempData.profileImage : profileData.profileImage) ? (
                    <img 
                      src={(isEditing ? tempData.profileImage : profileData.profileImage) || undefined} 
                      alt="Profile" 
                      className="w-full h-full object-cover"
                    />
                  ) : (
                    <User className="h-16 w-16 text-blue-500" />
                  )}
                </div>
              </div>
              {isEditing && (
                <motion.button
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  onClick={() => fileInputRef.current?.click()}
                  className="absolute bottom-0 right-0 w-10 h-10 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 text-white flex items-center justify-center shadow-lg hover:shadow-xl transition-shadow"
                >
                  <Camera className="h-5 w-5" />
                </motion.button>
              )}
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*"
                className="hidden"
                onChange={handleImageUpload}
              />
            </motion.div>
          </div>

          {/* User Name */}
          <motion.div 
            className="text-center mb-8"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2 }}
          >
            <h2 className="text-gray-900 mb-1">
              {isEditing ? tempData.name : profileData.name}
            </h2>
            <p className="text-gray-600">{profileData.email}</p>
          </motion.div>

          {/* Quick Stats */}
          <motion.div 
            className="grid grid-cols-3 gap-3 mb-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
          >
            <Card className="border-blue-200 bg-gradient-to-br from-blue-50 to-blue-100/50 backdrop-blur-sm shadow-sm">
              <CardContent className="p-4 text-center">
                <div className="w-10 h-10 mx-auto mb-2 bg-blue-500 rounded-xl flex items-center justify-center">
                  <Heart className="h-5 w-5 text-white" />
                </div>
                <p className="text-gray-900">{isEditing ? tempData.age : profileData.age}</p>
                <p className="text-gray-600">Age</p>
              </CardContent>
            </Card>

            <Card className="border-red-200 bg-gradient-to-br from-red-50 to-red-100/50 backdrop-blur-sm shadow-sm">
              <CardContent className="p-4 text-center">
                <div className="w-10 h-10 mx-auto mb-2 bg-red-500 rounded-xl flex items-center justify-center">
                  <Droplet className="h-5 w-5 text-white" />
                </div>
                <p className="text-gray-900">{isEditing ? tempData.bloodGroup : profileData.bloodGroup}</p>
                <p className="text-gray-600">Blood</p>
              </CardContent>
            </Card>

            <Card className="border-purple-200 bg-gradient-to-br from-purple-50 to-purple-100/50 backdrop-blur-sm shadow-sm">
              <CardContent className="p-4 text-center">
                <div className="w-10 h-10 mx-auto mb-2 bg-purple-500 rounded-xl flex items-center justify-center">
                  <Briefcase className="h-5 w-5 text-white" />
                </div>
                <p className="text-gray-900">10</p>
                <p className="text-gray-600">Records</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Profile Form */}
          <AnimatePresence mode="wait">
            <motion.div
              key={isEditing ? "editing" : "viewing"}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              transition={{ duration: 0.3 }}
            >
              <Card className="border-gray-200/50 bg-white/70 backdrop-blur-md shadow-lg">
                <CardContent className="p-6 space-y-5">
                  {/* Personal Information Section */}
                  <div className="space-y-4">
                    <div className="flex items-center gap-2 mb-4">
                      <Shield className="h-5 w-5 text-blue-600" />
                      <h3 className="text-gray-900">Personal Information</h3>
                    </div>

                    {/* Name */}
                    <div className="space-y-2">
                      <Label htmlFor="name" className="text-gray-700">Full Name</Label>
                      {isEditing ? (
                        <Input
                          id="name"
                          value={tempData.name}
                          onChange={(e) => setTempData({ ...tempData, name: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors"
                          placeholder="Enter your full name"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.name}</p>
                        </div>
                      )}
                    </div>

                    {/* Age */}
                    <div className="space-y-2">
                      <Label htmlFor="age" className="text-gray-700">Age</Label>
                      {isEditing ? (
                        <Input
                          id="age"
                          type="number"
                          value={tempData.age}
                          onChange={(e) => setTempData({ ...tempData, age: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors"
                          placeholder="Enter your age"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.age}</p>
                        </div>
                      )}
                    </div>

                    {/* Blood Group */}
                    <div className="space-y-2">
                      <Label htmlFor="bloodGroup" className="text-gray-700">Blood Group</Label>
                      {isEditing ? (
                        <Select
                          value={tempData.bloodGroup}
                          onValueChange={(value) => setTempData({ ...tempData, bloodGroup: value })}
                        >
                          <SelectTrigger className="bg-white border-gray-300 focus:border-blue-500">
                            <SelectValue placeholder="Select blood group" />
                          </SelectTrigger>
                          <SelectContent>
                            {bloodGroups.map((group) => (
                              <SelectItem key={group} value={group}>
                                {group}
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.bloodGroup}</p>
                        </div>
                      )}
                    </div>

                    {/* Email */}
                    <div className="space-y-2">
                      <Label htmlFor="email" className="text-gray-700">Email Address</Label>
                      {isEditing ? (
                        <Input
                          id="email"
                          type="email"
                          value={tempData.email}
                          onChange={(e) => setTempData({ ...tempData, email: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors"
                          placeholder="Enter your email"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.email}</p>
                        </div>
                      )}
                    </div>

                    {/* Phone */}
                    <div className="space-y-2">
                      <Label htmlFor="phone" className="text-gray-700">Phone Number</Label>
                      {isEditing ? (
                        <Input
                          id="phone"
                          type="tel"
                          value={tempData.phone}
                          onChange={(e) => setTempData({ ...tempData, phone: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors"
                          placeholder="Enter your phone number"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.phone}</p>
                        </div>
                      )}
                    </div>
                  </div>

                  {/* Medical Information Section */}
                  <div className="space-y-4 pt-4 border-t border-gray-200">
                    <div className="flex items-center gap-2 mb-4">
                      <Heart className="h-5 w-5 text-red-600" />
                      <h3 className="text-gray-900">Medical Information</h3>
                    </div>

                    {/* Permanent Illness */}
                    <div className="space-y-2">
                      <Label htmlFor="illness" className="text-gray-700">
                        Permanent Illness / Chronic Conditions
                      </Label>
                      {isEditing ? (
                        <Textarea
                          id="illness"
                          value={tempData.permanentIllness}
                          onChange={(e) => setTempData({ ...tempData, permanentIllness: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors min-h-[80px]"
                          placeholder="Enter any permanent illness or chronic conditions (e.g., diabetes, hypertension, asthma)"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200 min-h-[80px]">
                          <p className="text-gray-900">{profileData.permanentIllness}</p>
                        </div>
                      )}
                    </div>

                    {/* Emergency Contact */}
                    <div className="space-y-2">
                      <Label htmlFor="emergency" className="text-gray-700">Emergency Contact</Label>
                      {isEditing ? (
                        <Input
                          id="emergency"
                          type="tel"
                          value={tempData.emergencyContact}
                          onChange={(e) => setTempData({ ...tempData, emergencyContact: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors"
                          placeholder="Enter emergency contact number"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200">
                          <p className="text-gray-900">{profileData.emergencyContact}</p>
                        </div>
                      )}
                    </div>

                    {/* Address */}
                    <div className="space-y-2">
                      <Label htmlFor="address" className="text-gray-700">Address</Label>
                      {isEditing ? (
                        <Textarea
                          id="address"
                          value={tempData.address}
                          onChange={(e) => setTempData({ ...tempData, address: e.target.value })}
                          className="bg-white border-gray-300 focus:border-blue-500 transition-colors min-h-[80px]"
                          placeholder="Enter your full address"
                        />
                      ) : (
                        <div className="px-4 py-3 bg-gray-50 rounded-lg border border-gray-200 min-h-[80px]">
                          <p className="text-gray-900">{profileData.address}</p>
                        </div>
                      )}
                    </div>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </AnimatePresence>

          {/* Action Buttons */}
          <motion.div 
            className="mt-6 space-y-3"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
          >
            {isEditing ? (
              <div className="grid grid-cols-2 gap-3">
                <Button
                  onClick={handleCancel}
                  variant="outline"
                  className="w-full border-gray-300 hover:bg-gray-100 transition-colors"
                >
                  Cancel
                </Button>
                <Button
                  onClick={handleSave}
                  className="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white shadow-lg hover:shadow-xl transition-all"
                >
                  <Check className="h-4 w-4 mr-2" />
                  Save Changes
                </Button>
              </div>
            ) : (
              <>
                <Button
                  onClick={handleEdit}
                  className="w-full bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700 text-white shadow-lg hover:shadow-xl transition-all"
                >
                  <Edit2 className="h-4 w-4 mr-2" />
                  Edit Profile
                </Button>
                <Button
                  onClick={() => setShowLogoutDialog(true)}
                  variant="outline"
                  className="w-full border-red-300 text-red-600 hover:bg-red-50 transition-colors"
                >
                  <LogOut className="h-4 w-4 mr-2" />
                  Log Out
                </Button>
              </>
            )}
          </motion.div>
        </motion.div>
      </main>

      {/* Logout Confirmation Dialog */}
      <AlertDialog open={showLogoutDialog} onOpenChange={setShowLogoutDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirm Logout</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to log out? You'll need to sign in again to access your account.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleLogout}
              className="bg-red-600 hover:bg-red-700 text-white"
            >
              Log Out
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}