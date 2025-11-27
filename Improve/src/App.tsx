import { useState, useEffect } from "react";
import { Home } from "./components/Home";
import { MedicineReminder } from "./components/MedicineReminder";
import { UploadRecord } from "./components/UploadRecord";
import { Profile } from "./components/Profile";
import { Splash } from "./components/Splash";
import { Auth } from "./components/Auth";
import { History } from "./components/History";
import { DoctorsAppointment } from "./components/DoctorsAppointment";
import { VaccinationRecord } from "./components/VaccinationRecord";

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

export default function App() {
  const [showSplash, setShowSplash] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentPage, setCurrentPage] = useState<"home" | "reminder" | "upload" | "profile" | "history" | "appointment" | "vaccination">("home");
  const [uploadedFiles, setUploadedFiles] = useState<UploadedFile[]>([]);
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([]);

  // Show splash screen for 5 seconds on initial load
  useEffect(() => {
    const timer = setTimeout(() => {
      setShowSplash(false);
    }, 5000);

    return () => clearTimeout(timer);
  }, []);

  // Handle login
  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  // Handle logout
  const handleLogout = () => {
    setIsAuthenticated(false);
    setCurrentPage("home");
  };

  // Handle file upload
  const handleFileUpload = (file: UploadedFile) => {
    setUploadedFiles((prev) => [file, ...prev]);
  };

  // Handle file delete
  const handleFileDelete = (id: string) => {
    setUploadedFiles((prev) => prev.filter((file) => file.id !== id));
  };

  // Handle vaccination add
  const handleVaccinationAdd = (vaccination: Vaccination) => {
    setVaccinations((prev) => [vaccination, ...prev]);
  };

  // Handle vaccination delete
  const handleVaccinationDelete = (id: string) => {
    setVaccinations((prev) => prev.filter((vac) => vac.id !== id));
  };

  // Show splash screen
  if (showSplash) {
    return <Splash />;
  }

  // Show auth screen if not authenticated
  if (!isAuthenticated) {
    return <Auth onLogin={handleLogin} />;
  }

  // Show main app pages
  if (currentPage === "reminder") {
    return <MedicineReminder onBack={() => setCurrentPage("home")} />;
  }

  if (currentPage === "upload") {
    return (
      <UploadRecord 
        onBack={() => setCurrentPage("home")} 
        onFileUpload={handleFileUpload}
        uploadedFiles={uploadedFiles}
        onDeleteFile={handleFileDelete}
      />
    );
  }

  if (currentPage === "profile") {
    return <Profile onBack={() => setCurrentPage("home")} onLogout={handleLogout} />;
  }

  if (currentPage === "history") {
    return (
      <History 
        onBack={() => setCurrentPage("home")} 
        uploadedFiles={uploadedFiles}
        onDeleteFile={handleFileDelete}
      />
    );
  }

  if (currentPage === "appointment") {
    return <DoctorsAppointment onBack={() => setCurrentPage("home")} />;
  }

  if (currentPage === "vaccination") {
    return (
      <VaccinationRecord 
        onBack={() => setCurrentPage("home")} 
        onVaccinationAdd={handleVaccinationAdd}
        onVaccinationDelete={handleVaccinationDelete}
      />
    );
  }

  return <Home 
    onNavigateToReminder={() => setCurrentPage("reminder")} 
    onNavigateToUpload={() => setCurrentPage("upload")}
    onNavigateToProfile={() => setCurrentPage("profile")}
    onNavigateToHistory={() => setCurrentPage("history")}
    onNavigateToAppointment={() => setCurrentPage("appointment")}
    onNavigateToVaccination={() => setCurrentPage("vaccination")}
    uploadedFiles={uploadedFiles}
    vaccinations={vaccinations}
  />;
}