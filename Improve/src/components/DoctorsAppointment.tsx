import { useState } from "react";
import { ArrowLeft, Calendar, Clock, User, Stethoscope, FileText, Plus, Trash2, CheckCircle, AlertCircle } from "lucide-react";
import { Card, CardContent } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Badge } from "./ui/badge";
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

interface Test {
  id: string;
  name: string;
}

interface Appointment {
  id: string;
  doctorName: string;
  specialty: string;
  date: string;
  time: string;
  notes: string;
  tests: Test[];
  createdAt: string;
}

interface DoctorsAppointmentProps {
  onBack: () => void;
}

export function DoctorsAppointment({ onBack }: DoctorsAppointmentProps) {
  const [doctorName, setDoctorName] = useState("");
  const [specialty, setSpecialty] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [notes, setNotes] = useState("");
  const [testName, setTestName] = useState("");
  const [tests, setTests] = useState<Test[]>([]);
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [deleteAppointmentId, setDeleteAppointmentId] = useState<string | null>(null);

  const handleAddTest = () => {
    if (!testName.trim()) {
      toast.error("Please enter a test name");
      return;
    }

    const newTest: Test = {
      id: Date.now().toString() + Math.random().toString(),
      name: testName.trim(),
    };

    setTests((prev) => [...prev, newTest]);
    setTestName("");
    toast.success("Test added successfully");
  };

  const handleRemoveTest = (id: string) => {
    setTests((prev) => prev.filter((test) => test.id !== id));
    toast.info("Test removed");
  };

  const handleSaveAppointment = () => {
    // Validation
    if (!doctorName.trim()) {
      toast.error("Please enter doctor's name");
      return;
    }
    if (!specialty.trim()) {
      toast.error("Please enter doctor's specialty");
      return;
    }
    if (!appointmentDate) {
      toast.error("Please select appointment date");
      return;
    }
    if (!appointmentTime) {
      toast.error("Please select appointment time");
      return;
    }

    const newAppointment: Appointment = {
      id: Date.now().toString() + Math.random().toString(),
      doctorName: doctorName.trim(),
      specialty: specialty.trim(),
      date: appointmentDate,
      time: appointmentTime,
      notes: notes.trim(),
      tests: [...tests],
      createdAt: new Date().toISOString(),
    };

    setAppointments((prev) => [newAppointment, ...prev]);

    // Reset form
    setDoctorName("");
    setSpecialty("");
    setAppointmentDate("");
    setAppointmentTime("");
    setNotes("");
    setTests([]);

    toast.success("Appointment scheduled successfully!", {
      description: `${doctorName} - ${new Date(appointmentDate).toLocaleDateString()}`,
    });
  };

  const confirmDeleteAppointment = (id: string) => {
    setDeleteAppointmentId(id);
  };

  const handleDeleteAppointment = () => {
    if (deleteAppointmentId) {
      setAppointments((prev) => prev.filter((apt) => apt.id !== deleteAppointmentId));
      toast.success("Appointment deleted successfully");
      setDeleteAppointmentId(null);
    }
  };

  const handleCancelDelete = () => {
    setDeleteAppointmentId(null);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      weekday: 'long', 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    });
  };

  const formatTime = (timeString: string) => {
    const [hours, minutes] = timeString.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour % 12 || 12;
    return `${displayHour}:${minutes} ${ampm}`;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Toaster position="top-center" />

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={deleteAppointmentId !== null} onOpenChange={(open) => !open && setDeleteAppointmentId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Cancel Appointment?</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this appointment? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={handleCancelDelete}>No, Keep It</AlertDialogCancel>
            <AlertDialogAction onClick={handleDeleteAppointment} className="bg-red-600 hover:bg-red-700">
              Yes, Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

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
              <h1 className="text-gray-900">Doctor's Appointment</h1>
              <p className="text-gray-600">Schedule and manage appointments</p>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6 pb-24">
        
        {/* Appointment Form */}
        <Card className="mb-6 border-blue-200 shadow-sm">
          <CardContent className="p-6">
            <h2 className="text-gray-900 mb-4 flex items-center gap-2">
              <Stethoscope className="h-5 w-5 text-blue-600" />
              Schedule New Appointment
            </h2>

            <div className="space-y-4">
              {/* Doctor Name */}
              <div className="space-y-2">
                <Label htmlFor="doctorName">Doctor's Name</Label>
                <div className="relative">
                  <User className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <Input
                    id="doctorName"
                    placeholder="e.g., Dr. John Smith"
                    value={doctorName}
                    onChange={(e) => setDoctorName(e.target.value)}
                    className="pl-10 border-gray-300"
                  />
                </div>
              </div>

              {/* Specialty */}
              <div className="space-y-2">
                <Label htmlFor="specialty">Specialty</Label>
                <div className="relative">
                  <Stethoscope className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <Input
                    id="specialty"
                    placeholder="e.g., Cardiologist, Dermatologist"
                    value={specialty}
                    onChange={(e) => setSpecialty(e.target.value)}
                    className="pl-10 border-gray-300"
                  />
                </div>
              </div>

              {/* Date and Time */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="appointmentDate">Date</Label>
                  <div className="relative">
                    <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="appointmentDate"
                      type="date"
                      value={appointmentDate}
                      onChange={(e) => setAppointmentDate(e.target.value)}
                      className="pl-10 border-gray-300"
                      min={new Date().toISOString().split('T')[0]}
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="appointmentTime">Time</Label>
                  <div className="relative">
                    <Clock className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="appointmentTime"
                      type="time"
                      value={appointmentTime}
                      onChange={(e) => setAppointmentTime(e.target.value)}
                      className="pl-10 border-gray-300"
                    />
                  </div>
                </div>
              </div>

              {/* Notes */}
              <div className="space-y-2">
                <Label htmlFor="notes">Additional Notes (Optional)</Label>
                <div className="relative">
                  <FileText className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
                  <textarea
                    id="notes"
                    placeholder="Any additional information..."
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    className="w-full min-h-[80px] pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Tests to Do Before Checkup */}
        <Card className="mb-6 border-purple-200 shadow-sm">
          <CardContent className="p-6">
            <h2 className="text-gray-900 mb-4 flex items-center gap-2">
              <FileText className="h-5 w-5 text-purple-600" />
              Tests Before Checkup
            </h2>

            {/* Add Test Input */}
            <div className="flex gap-2 mb-4">
              <Input
                placeholder="e.g., Blood Test, X-Ray, ECG"
                value={testName}
                onChange={(e) => setTestName(e.target.value)}
                onKeyPress={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault();
                    handleAddTest();
                  }
                }}
                className="flex-1 border-gray-300"
              />
              <Button
                onClick={handleAddTest}
                className="bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700"
              >
                <Plus className="h-4 w-4 mr-2" />
                Add Test
              </Button>
            </div>

            {/* Tests List */}
            {tests.length > 0 ? (
              <div className="space-y-2">
                {tests.map((test) => (
                  <div
                    key={test.id}
                    className="flex items-center justify-between p-3 bg-purple-50 border border-purple-200 rounded-lg"
                  >
                    <div className="flex items-center gap-3">
                      <CheckCircle className="h-5 w-5 text-purple-600" />
                      <span className="text-gray-900">{test.name}</span>
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleRemoveTest(test.id)}
                      className="text-red-600 hover:text-red-700 hover:bg-red-50"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-6 text-gray-500">
                <FileText className="h-12 w-12 mx-auto mb-2 text-gray-300" />
                <p>No tests added yet</p>
                <p className="text-gray-400">Add tests that need to be done before your appointment</p>
              </div>
            )}
          </CardContent>
        </Card>

        {/* Save Button */}
        <Button
          onClick={handleSaveAppointment}
          className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 py-6 mb-6"
        >
          <Calendar className="h-5 w-5 mr-2" />
          Schedule Appointment
        </Button>

        {/* Scheduled Appointments */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-gray-900">Scheduled Appointments ({appointments.length})</h2>
            {appointments.length > 0 && (
              <Badge className="bg-blue-100 text-blue-800">
                {appointments.length} appointment{appointments.length !== 1 ? "s" : ""}
              </Badge>
            )}
          </div>

          {appointments.length === 0 ? (
            <Card className="border-gray-200">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                  <Calendar className="h-8 w-8 text-gray-400" />
                </div>
                <p className="text-gray-600 mb-2">No appointments scheduled</p>
                <p className="text-gray-500">Schedule your first appointment above</p>
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-4">
              {appointments.map((appointment) => (
                <Card
                  key={appointment.id}
                  className="border-gray-200 shadow-sm hover:shadow-md transition-all"
                >
                  <CardContent className="p-5">
                    <div className="flex items-start justify-between gap-4 mb-4">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <div className="w-12 h-12 bg-gradient-to-br from-blue-100 to-purple-100 rounded-xl flex items-center justify-center">
                            <Stethoscope className="h-6 w-6 text-blue-600" />
                          </div>
                          <div>
                            <h3 className="text-gray-900">{appointment.doctorName}</h3>
                            <p className="text-gray-600">{appointment.specialty}</p>
                          </div>
                        </div>

                        <div className="space-y-2 ml-15">
                          <div className="flex items-center gap-2 text-gray-700">
                            <Calendar className="h-4 w-4 text-blue-600" />
                            <span>{formatDate(appointment.date)}</span>
                          </div>
                          <div className="flex items-center gap-2 text-gray-700">
                            <Clock className="h-4 w-4 text-blue-600" />
                            <span>{formatTime(appointment.time)}</span>
                          </div>
                        </div>

                        {appointment.notes && (
                          <div className="mt-3 p-3 bg-gray-50 rounded-lg">
                            <p className="text-gray-700">{appointment.notes}</p>
                          </div>
                        )}

                        {appointment.tests.length > 0 && (
                          <div className="mt-4">
                            <div className="flex items-center gap-2 mb-2">
                              <FileText className="h-4 w-4 text-purple-600" />
                              <span className="text-gray-900">Tests Required:</span>
                            </div>
                            <div className="flex flex-wrap gap-2">
                              {appointment.tests.map((test) => (
                                <Badge
                                  key={test.id}
                                  variant="secondary"
                                  className="bg-purple-100 text-purple-800"
                                >
                                  {test.name}
                                </Badge>
                              ))}
                            </div>
                          </div>
                        )}
                      </div>

                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => confirmDeleteAppointment(appointment.id)}
                        className="text-red-600 hover:text-red-700 hover:bg-red-50"
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>

        {/* Info Card */}
        {appointments.length > 0 && (
          <Card className="mt-6 border-amber-200 bg-gradient-to-br from-amber-50 to-orange-50">
            <CardContent className="p-5">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-amber-100 rounded-xl flex items-center justify-center flex-shrink-0">
                  <AlertCircle className="h-6 w-6 text-amber-600" />
                </div>
                <div className="flex-1">
                  <h3 className="text-gray-900 mb-2">💡 Reminder</h3>
                  <p className="text-gray-700">
                    Make sure to complete all required tests before your appointment. Arrive 15 minutes early and bring your ID and insurance card.
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </main>
    </div>
  );
}
