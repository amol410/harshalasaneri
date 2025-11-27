import { Bell, Search, Upload, User, FileText, CheckCircle, Lightbulb, Activity, Plus, Clock, Pill, Syringe, Image as ImageIcon, Calendar } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Avatar, AvatarFallback } from "./ui/avatar";

interface UploadedFile {
  id: string;
  name: string;
  type: string;
  uploadDate: string;
  size: string;
  url: string;
}

interface Vaccination {
  id: string;
  vaccineName: string;
  doseNumber: string;
  dateAdministered: string;
  createdAt: string;
}

export function Home({ 
  onNavigateToReminder, 
  onNavigateToUpload, 
  onNavigateToProfile,
  onNavigateToHistory,
  onNavigateToAppointment,
  onNavigateToVaccination,
  uploadedFiles,
  vaccinations 
}: { 
  onNavigateToReminder: () => void; 
  onNavigateToUpload: () => void;
  onNavigateToProfile: () => void;
  onNavigateToHistory: () => void;
  onNavigateToAppointment: () => void;
  onNavigateToVaccination: () => void;
  uploadedFiles: UploadedFile[];
  vaccinations: Vaccination[];
}) {
  // Get 2 most recent vaccinations
  const recentVaccinations = vaccinations.slice(0, 2);

  // Get 2 most recent uploaded files
  const recentUploads = uploadedFiles.slice(0, 2);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-white/80 backdrop-blur-lg border-b border-gray-200/50 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Avatar className="h-12 w-12 border-2 border-blue-200">
                <AvatarFallback className="bg-gradient-to-br from-blue-500 to-purple-600 text-white">
                  <User className="h-5 w-5" />
                </AvatarFallback>
              </Avatar>
              <div>
                <h1 className="text-gray-900">Welcome, userName</h1>
                <p className="text-gray-600">How are you feeling today?</p>
              </div>
            </div>
            <Button variant="ghost" size="icon" className="relative">
              <Bell className="h-5 w-5 text-gray-600" />
              <span className="absolute top-1 right-1 h-2 w-2 bg-red-500 rounded-full"></span>
            </Button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6 pb-24">
        
        {/* Quick Actions */}
        <section className="mb-8">
          <h2 className="text-gray-900 mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <button 
              onClick={onNavigateToReminder}
              className="group bg-white rounded-2xl p-6 shadow-sm hover:shadow-lg transition-all duration-300 border border-gray-100 hover:border-blue-200 hover:-translate-y-1"
            >
              <div className="w-14 h-14 mx-auto mb-3 bg-gradient-to-br from-blue-100 to-blue-50 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                <Pill className="h-6 w-6 text-blue-600" />
              </div>
              <p className="text-gray-900 text-center">Set Reminder</p>
            </button>

            <button 
              onClick={onNavigateToUpload}
              className="group bg-white rounded-2xl p-6 shadow-sm hover:shadow-lg transition-all duration-300 border border-gray-100 hover:border-purple-200 hover:-translate-y-1"
            >
              <div className="w-14 h-14 mx-auto mb-3 bg-gradient-to-br from-purple-100 to-purple-50 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                <Upload className="h-6 w-6 text-purple-600" />
              </div>
              <p className="text-gray-900 text-center">Upload Record</p>
            </button>

            <button 
              onClick={onNavigateToVaccination}
              className="group bg-white rounded-2xl p-6 shadow-sm hover:shadow-lg transition-all duration-300 border border-gray-100 hover:border-green-200 hover:-translate-y-1"
            >
              <div className="w-14 h-14 mx-auto mb-3 bg-gradient-to-br from-green-100 to-green-50 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                <Syringe className="h-6 w-6 text-green-600" />
              </div>
              <p className="text-gray-900 text-center">Vaccination</p>
            </button>

            <button 
              onClick={onNavigateToProfile}
              className="group bg-white rounded-2xl p-6 shadow-sm hover:shadow-lg transition-all duration-300 border border-gray-100 hover:border-teal-200 hover:-translate-y-1"
            >
              <div className="w-14 h-14 mx-auto mb-3 bg-gradient-to-br from-teal-100 to-teal-50 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                <User className="h-6 w-6 text-teal-600" />
              </div>
              <p className="text-gray-900 text-center">Update Profile</p>
            </button>
          </div>
        </section>

        {/* Recommendation Card */}
        <section className="mb-8">
          <h2 className="text-gray-900 mb-4">Recommendation</h2>
          <Card className="border-amber-200 bg-gradient-to-br from-amber-50 to-orange-50 shadow-sm hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start gap-3">
                <div className="w-10 h-10 bg-amber-100 rounded-lg flex items-center justify-center flex-shrink-0">
                  <Activity className="h-5 w-5 text-amber-700" />
                </div>
                <div className="flex-1">
                  <CardTitle className="text-gray-900">For illness this days</CardTitle>
                  <Badge variant="secondary" className="mt-2 bg-amber-100 text-amber-800 hover:bg-amber-100">
                    Health Alert
                  </Badge>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <CardDescription className="text-gray-700">
                Based on current health trends, make sure to get adequate rest and maintain good hygiene practices.
              </CardDescription>
            </CardContent>
          </Card>
        </section>

        {/* Healthy Health Benefits */}
        <section className="mb-8">
          <Card className="border-emerald-200 bg-gradient-to-br from-emerald-50 to-teal-50 shadow-sm">
            <CardHeader>
              <CardTitle className="text-gray-900 flex items-center gap-2">
                <Lightbulb className="h-5 w-5 text-emerald-600" />
                Healthy Health Benefits
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <div className="flex items-start gap-3">
                <CheckCircle className="h-5 w-5 text-emerald-600 mt-0.5 flex-shrink-0" />
                <p className="text-gray-700">Regular exercise improves cardiovascular health</p>
              </div>
              <div className="flex items-start gap-3">
                <CheckCircle className="h-5 w-5 text-emerald-600 mt-0.5 flex-shrink-0" />
                <p className="text-gray-700">Balanced diet boosts immune system</p>
              </div>
              <div className="flex items-start gap-3">
                <CheckCircle className="h-5 w-5 text-emerald-600 mt-0.5 flex-shrink-0" />
                <p className="text-gray-700">Adequate sleep enhances mental clarity</p>
              </div>
            </CardContent>
          </Card>
        </section>

        {/* Recent Activity */}
        <section className="mb-8">
          <h2 className="text-gray-900 mb-4">Recent Activity</h2>
          <div className="space-y-3">
            {/* Recent Vaccinations */}
            {recentVaccinations.map(vaccination => (
              <Card key={vaccination.id} className="border-gray-200 hover:border-green-300 transition-colors shadow-sm hover:shadow-md">
                <CardContent className="p-4">
                  <div className="flex items-center gap-4">
                    <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center flex-shrink-0">
                      <Syringe className="h-6 w-6 text-green-600" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <h3 className="text-gray-900">Vaccination</h3>
                      <p className="text-gray-600">{vaccination.dateAdministered} • {vaccination.vaccineName}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}

            {/* Recent Uploads */}
            {recentUploads.map(file => {
              const formatDate = (dateString: string) => {
                const date = new Date(dateString);
                const now = new Date();
                const diffTime = Math.abs(now.getTime() - date.getTime());
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                
                if (diffDays === 0) return 'Today';
                if (diffDays === 1) return 'Yesterday';
                if (diffDays < 7) return `${diffDays} days ago`;
                return date.toLocaleDateString();
              };

              return (
                <Card key={file.id} className="border-gray-200 hover:border-purple-300 transition-colors shadow-sm hover:shadow-md">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center flex-shrink-0">
                        {file.type === 'image' ? (
                          <ImageIcon className="h-6 w-6 text-purple-600" />
                        ) : (
                          <FileText className="h-6 w-6 text-purple-600" />
                        )}
                      </div>
                      <div className="flex-1 min-w-0">
                        <h3 className="text-gray-900">New Record Added</h3>
                        <p className="text-gray-600">{formatDate(file.uploadDate)} • {file.name}</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              );
            })}

            {/* Empty State */}
            {recentVaccinations.length === 0 && recentUploads.length === 0 && (
              <Card className="border-gray-200 shadow-sm">
                <CardContent className="p-8 text-center">
                  <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                    <Activity className="h-8 w-8 text-gray-400" />
                  </div>
                  <h3 className="text-gray-900 mb-2">No Recent Activity</h3>
                  <p className="text-gray-600">Your recent vaccinations and uploaded records will appear here</p>
                </CardContent>
              </Card>
            )}
          </div>
        </section>

        {/* Daily Health Tip */}
        <section>
          <Card className="border-blue-200 bg-gradient-to-br from-blue-50 to-indigo-50 shadow-sm">
            <CardContent className="p-5">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
                  <Lightbulb className="h-6 w-6 text-blue-600" />
                </div>
                <div className="flex-1">
                  <h3 className="text-gray-900 mb-2">Daily Health Tip</h3>
                  <p className="text-gray-700">
                    Stay hydrated! Drink at least 8 glasses of water today.
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </section>
      </main>

      {/* Bottom Navigation */}
      <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 shadow-lg z-50">
        <div className="max-w-7xl mx-auto px-4">
          <div className="grid grid-cols-3 gap-1">
            <button className="flex flex-col items-center gap-1 py-3 text-blue-600 relative">
              <div className="absolute top-0 left-1/2 -translate-x-1/2 w-12 h-1 bg-blue-600 rounded-full"></div>
              <div className="w-10 h-10 flex items-center justify-center">
                <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
                </svg>
              </div>
              <span className="text-gray-900">Home</span>
            </button>

            <button 
              onClick={onNavigateToAppointment}
              className="flex flex-col items-center gap-1 py-3 text-gray-500 hover:text-gray-900 transition-colors"
            >
              <div className="w-10 h-10 flex items-center justify-center">
                <Calendar className="h-6 w-6" />
              </div>
              <span className="text-gray-600">Appointment</span>
            </button>

            <button 
              onClick={onNavigateToHistory}
              className="flex flex-col items-center gap-1 py-3 text-gray-500 hover:text-gray-900 transition-colors"
            >
              <div className="w-10 h-10 flex items-center justify-center">
                <Clock className="h-6 w-6" />
              </div>
              <span className="text-gray-600">History</span>
            </button>
          </div>
        </div>
      </nav>
    </div>
  );
}