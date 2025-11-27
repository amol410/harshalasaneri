import { useState } from "react";
import { ArrowLeft, Calendar, Syringe, User, MapPin, FileText, Plus, Trash2, CheckCircle, Clock, AlertCircle } from "lucide-react";
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

interface Vaccination {
  id: string;
  vaccineName: string;
  doseNumber: string;
  dateAdministered: string;
  nextDueDate: string;
  administeredBy: string;
  location: string;
  batchNumber: string;
  notes: string;
  createdAt: string;
}

interface VaccinationRecordProps {
  onBack: () => void;
  onVaccinationAdd: (vaccination: { id: string; vaccineName: string; doseNumber: string; dateAdministered: string; createdAt: string; }) => void;
  onVaccinationDelete: (id: string) => void;
}

export function VaccinationRecord({ onBack, onVaccinationAdd, onVaccinationDelete }: VaccinationRecordProps) {
  const [vaccineName, setVaccineName] = useState("");
  const [doseNumber, setDoseNumber] = useState("");
  const [dateAdministered, setDateAdministered] = useState("");
  const [nextDueDate, setNextDueDate] = useState("");
  const [administeredBy, setAdministeredBy] = useState("");
  const [location, setLocation] = useState("");
  const [batchNumber, setBatchNumber] = useState("");
  const [notes, setNotes] = useState("");
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([]);
  const [deleteVaccinationId, setDeleteVaccinationId] = useState<string | null>(null);

  const handleSaveVaccination = () => {
    // Validation
    if (!vaccineName.trim()) {
      toast.error("Please enter vaccine name");
      return;
    }
    if (!doseNumber.trim()) {
      toast.error("Please enter dose number");
      return;
    }
    if (!dateAdministered) {
      toast.error("Please select date administered");
      return;
    }
    if (!administeredBy.trim()) {
      toast.error("Please enter who administered the vaccine");
      return;
    }
    if (!location.trim()) {
      toast.error("Please enter location");
      return;
    }

    const newVaccination: Vaccination = {
      id: Date.now().toString() + Math.random().toString(),
      vaccineName: vaccineName.trim(),
      doseNumber: doseNumber.trim(),
      dateAdministered: dateAdministered,
      nextDueDate: nextDueDate,
      administeredBy: administeredBy.trim(),
      location: location.trim(),
      batchNumber: batchNumber.trim(),
      notes: notes.trim(),
      createdAt: new Date().toISOString(),
    };

    setVaccinations((prev) => [newVaccination, ...prev]);

    // Reset form
    setVaccineName("");
    setDoseNumber("");
    setDateAdministered("");
    setNextDueDate("");
    setAdministeredBy("");
    setLocation("");
    setBatchNumber("");
    setNotes("");

    toast.success("Vaccination record added successfully!", {
      description: `${vaccineName} - ${doseNumber}`,
    });

    // Call the onVaccinationAdd callback
    onVaccinationAdd(newVaccination);
  };

  const confirmDeleteVaccination = (id: string) => {
    setDeleteVaccinationId(id);
  };

  const handleDeleteVaccination = () => {
    if (deleteVaccinationId) {
      setVaccinations((prev) => prev.filter((vac) => vac.id !== deleteVaccinationId));
      toast.success("Vaccination record deleted successfully");
      setDeleteVaccinationId(null);

      // Call the onVaccinationDelete callback
      onVaccinationDelete(deleteVaccinationId);
    }
  };

  const handleCancelDelete = () => {
    setDeleteVaccinationId(null);
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    });
  };

  const isUpcoming = (dateString: string) => {
    if (!dateString) return false;
    const nextDate = new Date(dateString);
    const today = new Date();
    const diffTime = nextDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays > 0 && diffDays <= 30;
  };

  const isOverdue = (dateString: string) => {
    if (!dateString) return false;
    const nextDate = new Date(dateString);
    const today = new Date();
    return nextDate < today;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Toaster position="top-center" />

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={deleteVaccinationId !== null} onOpenChange={(open) => !open && setDeleteVaccinationId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Vaccination Record?</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this vaccination record? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={handleCancelDelete}>No, Keep It</AlertDialogCancel>
            <AlertDialogAction onClick={handleDeleteVaccination} className="bg-red-600 hover:bg-red-700">
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
              <h1 className="text-gray-900">Vaccination Record</h1>
              <p className="text-gray-600">Track your immunization history</p>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6 pb-24">
        
        {/* Vaccination Form */}
        <Card className="mb-6 border-green-200 shadow-sm">
          <CardContent className="p-6">
            <h2 className="text-gray-900 mb-4 flex items-center gap-2">
              <Syringe className="h-5 w-5 text-green-600" />
              Add Vaccination Record
            </h2>

            <div className="space-y-4">
              {/* Vaccine Name & Dose */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="vaccineName">Vaccine Name</Label>
                  <Input
                    id="vaccineName"
                    placeholder="e.g., COVID-19, Hepatitis B, MMR"
                    value={vaccineName}
                    onChange={(e) => setVaccineName(e.target.value)}
                    className="border-gray-300"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="doseNumber">Dose Number</Label>
                  <Input
                    id="doseNumber"
                    placeholder="e.g., 1st Dose, 2nd Dose, Booster"
                    value={doseNumber}
                    onChange={(e) => setDoseNumber(e.target.value)}
                    className="border-gray-300"
                  />
                </div>
              </div>

              {/* Dates */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="dateAdministered">Date Administered</Label>
                  <div className="relative">
                    <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="dateAdministered"
                      type="date"
                      value={dateAdministered}
                      onChange={(e) => setDateAdministered(e.target.value)}
                      className="pl-10 border-gray-300"
                      max={new Date().toISOString().split('T')[0]}
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="nextDueDate">Next Dose Due Date (Optional)</Label>
                  <div className="relative">
                    <Clock className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="nextDueDate"
                      type="date"
                      value={nextDueDate}
                      onChange={(e) => setNextDueDate(e.target.value)}
                      className="pl-10 border-gray-300"
                      min={new Date().toISOString().split('T')[0]}
                    />
                  </div>
                </div>
              </div>

              {/* Administered By & Location */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="administeredBy">Administered By</Label>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="administeredBy"
                      placeholder="e.g., Dr. Smith, City Hospital"
                      value={administeredBy}
                      onChange={(e) => setAdministeredBy(e.target.value)}
                      className="pl-10 border-gray-300"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="location">Location</Label>
                  <div className="relative">
                    <MapPin className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input
                      id="location"
                      placeholder="e.g., City Health Center"
                      value={location}
                      onChange={(e) => setLocation(e.target.value)}
                      className="pl-10 border-gray-300"
                    />
                  </div>
                </div>
              </div>

              {/* Batch Number */}
              <div className="space-y-2">
                <Label htmlFor="batchNumber">Batch/Lot Number (Optional)</Label>
                <Input
                  id="batchNumber"
                  placeholder="e.g., ABC12345"
                  value={batchNumber}
                  onChange={(e) => setBatchNumber(e.target.value)}
                  className="border-gray-300"
                />
              </div>

              {/* Notes */}
              <div className="space-y-2">
                <Label htmlFor="notes">Additional Notes (Optional)</Label>
                <div className="relative">
                  <FileText className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
                  <textarea
                    id="notes"
                    placeholder="Any side effects, reactions, or additional information..."
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    className="w-full min-h-[80px] pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Save Button */}
        <Button
          onClick={handleSaveVaccination}
          className="w-full bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 py-6 mb-6"
        >
          <CheckCircle className="h-5 w-5 mr-2" />
          Save Vaccination Record
        </Button>

        {/* Vaccination Records */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-gray-900">Vaccination History ({vaccinations.length})</h2>
            {vaccinations.length > 0 && (
              <Badge className="bg-green-100 text-green-800">
                {vaccinations.length} record{vaccinations.length !== 1 ? "s" : ""}
              </Badge>
            )}
          </div>

          {vaccinations.length === 0 ? (
            <Card className="border-gray-200">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                  <Syringe className="h-8 w-8 text-gray-400" />
                </div>
                <p className="text-gray-600 mb-2">No vaccination records yet</p>
                <p className="text-gray-500">Add your first vaccination record above</p>
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-4">
              {vaccinations.map((vaccination) => (
                <Card
                  key={vaccination.id}
                  className="border-gray-200 shadow-sm hover:shadow-md transition-all"
                >
                  <CardContent className="p-5">
                    <div className="flex items-start justify-between gap-4">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-3">
                          <div className="w-12 h-12 bg-gradient-to-br from-green-100 to-emerald-100 rounded-xl flex items-center justify-center">
                            <Syringe className="h-6 w-6 text-green-600" />
                          </div>
                          <div>
                            <h3 className="text-gray-900">{vaccination.vaccineName}</h3>
                            <p className="text-gray-600">{vaccination.doseNumber}</p>
                          </div>
                        </div>

                        <div className="space-y-2 ml-15">
                          <div className="flex items-center gap-2 text-gray-700">
                            <Calendar className="h-4 w-4 text-green-600" />
                            <span>Administered: {formatDate(vaccination.dateAdministered)}</span>
                          </div>

                          {vaccination.nextDueDate && (
                            <div className="flex items-center gap-2">
                              <Clock className="h-4 w-4 text-blue-600" />
                              <span className="text-gray-700">Next Due: {formatDate(vaccination.nextDueDate)}</span>
                              {isOverdue(vaccination.nextDueDate) && (
                                <Badge variant="destructive" className="bg-red-100 text-red-800">
                                  Overdue
                                </Badge>
                              )}
                              {isUpcoming(vaccination.nextDueDate) && (
                                <Badge className="bg-amber-100 text-amber-800">
                                  Upcoming
                                </Badge>
                              )}
                            </div>
                          )}

                          <div className="flex items-center gap-2 text-gray-700">
                            <User className="h-4 w-4 text-purple-600" />
                            <span>By: {vaccination.administeredBy}</span>
                          </div>

                          <div className="flex items-center gap-2 text-gray-700">
                            <MapPin className="h-4 w-4 text-orange-600" />
                            <span>At: {vaccination.location}</span>
                          </div>

                          {vaccination.batchNumber && (
                            <div className="text-gray-600">
                              Batch: {vaccination.batchNumber}
                            </div>
                          )}
                        </div>

                        {vaccination.notes && (
                          <div className="mt-3 p-3 bg-gray-50 rounded-lg">
                            <p className="text-gray-700">{vaccination.notes}</p>
                          </div>
                        )}
                      </div>

                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => confirmDeleteVaccination(vaccination.id)}
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
        {vaccinations.length > 0 && (
          <Card className="mt-6 border-blue-200 bg-gradient-to-br from-blue-50 to-indigo-50">
            <CardContent className="p-5">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
                  <AlertCircle className="h-6 w-6 text-blue-600" />
                </div>
                <div className="flex-1">
                  <h3 className="text-gray-900 mb-2">💉 Vaccination Tip</h3>
                  <p className="text-gray-700">
                    Keep your vaccination records up to date. Set reminders for booster shots and follow-up doses. Always consult with your healthcare provider about recommended vaccinations.
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