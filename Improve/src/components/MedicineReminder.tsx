import { useState, useEffect } from "react";
import { ArrowLeft, Plus, Trash2, Bell, Clock, Pill, MessageSquare, Calendar } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Badge } from "./ui/badge";
import { toast } from "sonner@2.0.3";
import { Toaster } from "./ui/sonner";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";

interface Reminder {
  id: string;
  medicineName: string;
  dosage: string;
  time: string;
  phoneNumber: string;
  active: boolean;
  durationType: "everyday" | "week" | "custom";
  startDate: string;
  endDate: string;
}

export function MedicineReminder({ onBack }: { onBack: () => void }) {
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    medicineName: "",
    dosage: "",
    time: "",
    phoneNumber: "",
    durationType: "everyday" as "everyday" | "week" | "custom",
    startDate: new Date().toISOString().split('T')[0],
    endDate: "",
  });

  // Check reminders every minute
  useEffect(() => {
    const interval = setInterval(() => {
      checkReminders();
    }, 60000); // Check every minute

    return () => clearInterval(interval);
  }, [reminders]);

  const checkReminders = () => {
    const now = new Date();
    const currentTime = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

    reminders.forEach((reminder) => {
      if (reminder.active && reminder.time === currentTime) {
        sendSMSNotification(reminder);
      }
    });
  };

  const sendSMSNotification = (reminder: Reminder) => {
    // Mock SMS sending - In production, this would call a backend API with Supabase
    console.log(`Sending SMS to ${reminder.phoneNumber}:`);
    console.log(`Time to take your medicine: ${reminder.medicineName} (${reminder.dosage})`);
    
    toast.success("Medicine Reminder", {
      description: `Time to take ${reminder.medicineName} (${reminder.dosage})! SMS sent to ${reminder.phoneNumber}`,
      icon: <Bell className="h-5 w-5" />,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.medicineName || !formData.time || !formData.phoneNumber) {
      toast.error("Please fill in all required fields");
      return;
    }

    // Calculate end date based on duration type
    let calculatedEndDate = formData.endDate;
    if (formData.durationType === "week") {
      const startDate = new Date(formData.startDate);
      startDate.setDate(startDate.getDate() + 7);
      calculatedEndDate = startDate.toISOString().split('T')[0];
    } else if (formData.durationType === "everyday") {
      calculatedEndDate = ""; // No end date for everyday
    }

    const newReminder: Reminder = {
      id: Date.now().toString(),
      medicineName: formData.medicineName,
      dosage: formData.dosage || "As prescribed",
      time: formData.time,
      phoneNumber: formData.phoneNumber,
      active: true,
      durationType: formData.durationType,
      startDate: formData.startDate,
      endDate: calculatedEndDate,
    };

    setReminders([...reminders, newReminder]);
    setFormData({ medicineName: "", dosage: "", time: "", phoneNumber: "", durationType: "everyday", startDate: new Date().toISOString().split('T')[0], endDate: "" });
    setShowForm(false);
    
    const durationText = formData.durationType === "everyday" ? "everyday" : 
                        formData.durationType === "week" ? "for one week" : 
                        `from ${formData.startDate} to ${formData.endDate}`;
    
    toast.success("Reminder Set!", {
      description: `You'll receive an SMS at ${formData.time} ${durationText} for ${formData.medicineName}`,
    });
  };

  const deleteReminder = (id: string) => {
    setReminders(reminders.filter((r) => r.id !== id));
    toast.success("Reminder deleted");
  };

  const toggleReminder = (id: string) => {
    setReminders(
      reminders.map((r) =>
        r.id === id ? { ...r, active: !r.active } : r
      )
    );
  };

  const getDurationText = (reminder: Reminder) => {
    if (reminder.durationType === "everyday") {
      return "Everyday (Ongoing)";
    } else if (reminder.durationType === "week") {
      return `For One Week (until ${new Date(reminder.endDate).toLocaleDateString()})`;
    } else {
      return `${new Date(reminder.startDate).toLocaleDateString()} - ${new Date(reminder.endDate).toLocaleDateString()}`;
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Toaster position="top-center" />
      
      {/* Header */}
      <header className="sticky top-0 z-50 bg-white/80 backdrop-blur-lg border-b border-gray-200/50 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="icon"
              onClick={onBack}
              className="rounded-full"
            >
              <ArrowLeft className="h-5 w-5" />
            </Button>
            <div className="flex-1">
              <h1 className="text-gray-900">Medicine Reminders</h1>
              <p className="text-gray-600">Never miss your medication</p>
            </div>
            <Button
              onClick={() => setShowForm(!showForm)}
              className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
            >
              <Plus className="h-5 w-5 mr-2" />
              Add New
            </Button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6 pb-24">
        
        {/* Add Reminder Form */}
        {showForm && (
          <Card className="mb-6 border-blue-200 shadow-lg">
            <CardHeader>
              <CardTitle className="text-gray-900 flex items-center gap-2">
                <Pill className="h-5 w-5 text-blue-600" />
                Add Medicine Reminder
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="medicineName">Medicine Name *</Label>
                  <Input
                    id="medicineName"
                    placeholder="e.g., Aspirin"
                    value={formData.medicineName}
                    onChange={(e) =>
                      setFormData({ ...formData, medicineName: e.target.value })
                    }
                    className="border-gray-300"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="dosage">Dosage</Label>
                  <Input
                    id="dosage"
                    placeholder="e.g., 1 tablet, 500mg"
                    value={formData.dosage}
                    onChange={(e) =>
                      setFormData({ ...formData, dosage: e.target.value })
                    }
                    className="border-gray-300"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="time">Time *</Label>
                  <Input
                    id="time"
                    type="time"
                    value={formData.time}
                    onChange={(e) =>
                      setFormData({ ...formData, time: e.target.value })
                    }
                    className="border-gray-300"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="phoneNumber">Phone Number (for SMS) *</Label>
                  <Input
                    id="phoneNumber"
                    type="tel"
                    placeholder="+1 234 567 8900"
                    value={formData.phoneNumber}
                    onChange={(e) =>
                      setFormData({ ...formData, phoneNumber: e.target.value })
                    }
                    className="border-gray-300"
                  />
                </div>

                <div className="space-y-2">
                  <Label>Duration Type *</Label>
                  <RadioGroup
                    value={formData.durationType}
                    onValueChange={(value) => setFormData({ ...formData, durationType: value as "everyday" | "week" | "custom" })}
                  >
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="everyday" id="everyday" />
                      <Label htmlFor="everyday" className="cursor-pointer">Everyday (Ongoing)</Label>
                    </div>
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="week" id="week" />
                      <Label htmlFor="week" className="cursor-pointer">For One Week</Label>
                    </div>
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="custom" id="custom" />
                      <Label htmlFor="custom" className="cursor-pointer">Custom Time Period</Label>
                    </div>
                  </RadioGroup>
                </div>

                {formData.durationType === "custom" && (
                  <div className="space-y-2">
                    <Label htmlFor="startDate">Start Date</Label>
                    <Input
                      id="startDate"
                      type="date"
                      value={formData.startDate}
                      onChange={(e) =>
                        setFormData({ ...formData, startDate: e.target.value })
                      }
                      className="border-gray-300"
                    />
                  </div>
                )}

                {formData.durationType === "custom" && (
                  <div className="space-y-2">
                    <Label htmlFor="endDate">End Date</Label>
                    <Input
                      id="endDate"
                      type="date"
                      value={formData.endDate}
                      onChange={(e) =>
                        setFormData({ ...formData, endDate: e.target.value })
                      }
                      className="border-gray-300"
                    />
                  </div>
                )}

                <div className="flex gap-3 pt-2">
                  <Button
                    type="submit"
                    className="flex-1 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
                  >
                    Set Reminder
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setShowForm(false)}
                    className="flex-1"
                  >
                    Cancel
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        {/* Info Card */}
        <Card className="mb-6 border-blue-200 bg-gradient-to-br from-blue-50 to-indigo-50">
          <CardContent className="p-5">
            <div className="flex items-start gap-4">
              <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
                <MessageSquare className="h-6 w-6 text-blue-600" />
              </div>
              <div className="flex-1">
                <h3 className="text-gray-900 mb-2">SMS Notifications</h3>
                <p className="text-gray-700">
                  You'll receive text message reminders at the scheduled times. Make sure to provide a valid phone number.
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Reminders List */}
        <div className="space-y-4">
          <h2 className="text-gray-900">Your Reminders ({reminders.length})</h2>
          
          {reminders.length === 0 ? (
            <Card className="border-gray-200">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                  <Pill className="h-8 w-8 text-gray-400" />
                </div>
                <p className="text-gray-600 mb-2">No reminders set yet</p>
                <p className="text-gray-500">Click "Add New" to create your first medicine reminder</p>
              </CardContent>
            </Card>
          ) : (
            reminders.map((reminder) => (
              <Card
                key={reminder.id}
                className={`border-gray-200 shadow-sm hover:shadow-md transition-all ${
                  !reminder.active ? "opacity-60" : ""
                }`}
              >
                <CardContent className="p-4">
                  <div className="flex items-start gap-4">
                    <div className={`w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0 ${
                      reminder.active 
                        ? "bg-gradient-to-br from-blue-100 to-purple-100" 
                        : "bg-gray-100"
                    }`}>
                      <Pill className={`h-6 w-6 ${reminder.active ? "text-blue-600" : "text-gray-400"}`} />
                    </div>
                    
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 mb-2">
                        <div>
                          <h3 className="text-gray-900">{reminder.medicineName}</h3>
                          <p className="text-gray-600">{reminder.dosage}</p>
                        </div>
                        {reminder.active && (
                          <Badge className="bg-green-100 text-green-800 hover:bg-green-100">
                            Active
                          </Badge>
                        )}
                      </div>
                      
                      <div className="flex items-center gap-4 text-gray-600 mb-3">
                        <div className="flex items-center gap-1">
                          <Clock className="h-4 w-4" />
                          <span>{reminder.time}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <MessageSquare className="h-4 w-4" />
                          <span>{reminder.phoneNumber}</span>
                        </div>
                      </div>
                      
                      <div className="mb-3 flex items-center gap-1 text-gray-600">
                        <Calendar className="h-4 w-4" />
                        <span>{getDurationText(reminder)}</span>
                      </div>
                      
                      <div className="flex gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => toggleReminder(reminder.id)}
                          className="text-gray-700"
                        >
                          <Bell className="h-4 w-4 mr-1" />
                          {reminder.active ? "Disable" : "Enable"}
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => deleteReminder(reminder.id)}
                          className="text-red-600 hover:text-red-700 hover:bg-red-50"
                        >
                          <Trash2 className="h-4 w-4 mr-1" />
                          Delete
                        </Button>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </div>

        {/* Demo Button for Testing */}
        {reminders.length > 0 && (
          <Card className="mt-6 border-amber-200 bg-gradient-to-br from-amber-50 to-orange-50">
            <CardContent className="p-5">
              <div className="flex items-start gap-4">
                <div className="flex-1">
                  <h3 className="text-gray-900 mb-2">Test Notification</h3>
                  <p className="text-gray-700 mb-3">
                    Click below to test the SMS notification for your reminders
                  </p>
                  <Button
                    onClick={() => reminders.length > 0 && sendSMSNotification(reminders[0])}
                    variant="outline"
                    className="border-amber-300 hover:bg-amber-100"
                  >
                    <MessageSquare className="h-4 w-4 mr-2" />
                    Send Test SMS
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </main>
    </div>
  );
}