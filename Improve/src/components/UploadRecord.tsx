import { useState, useRef } from "react";
import { ArrowLeft, Upload, Camera, FileText, Image as ImageIcon, Trash2, Download, Eye, Crown, AlertCircle, CheckCircle } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
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
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";

interface UploadedFile {
  id: string;
  name: string;
  type: string;
  uploadDate: string;
  size: string;
  url: string;
}

interface UploadRecordProps {
  onBack: () => void;
  onFileUpload: (file: UploadedFile) => void;
  uploadedFiles: UploadedFile[];
  onDeleteFile: (id: string) => void;
}

export function UploadRecord({ onBack, onFileUpload, uploadedFiles, onDeleteFile }: UploadRecordProps) {
  const [deleteFileId, setDeleteFileId] = useState<string | null>(null);
  const [showUpgradeDialog, setShowUpgradeDialog] = useState(false);
  const [recordTitle, setRecordTitle] = useState(""); // Add missing state
  const fileInputRef = useRef<HTMLInputElement>(null);
  const cameraInputRef = useRef<HTMLInputElement>(null);

  const MAX_FREE_FILES = 10;

  const checkFileLimit = (newFilesCount: number = 1) => {
    if (uploadedFiles.length + newFilesCount > MAX_FREE_FILES) {
      setShowUpgradeDialog(true);
      return false;
    }
    return true;
  };

  const formatFileSize = (bytes: number): string => {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFiles = e.target.files;
    if (!selectedFiles) return;

    const filesToAdd = Array.from(selectedFiles);
    
    // Check if adding these files would exceed the limit
    if (!checkFileLimit(filesToAdd.length)) {
      e.target.value = "";
      return;
    }

    filesToAdd.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        const newFile: UploadedFile = {
          id: Date.now().toString() + Math.random().toString(),
          name: file.name,
          type: file.type.startsWith('image/') ? 'image' : 'document',
          url: event.target?.result as string,
          size: formatFileSize(file.size),
          uploadDate: new Date().toISOString(),
        };
        onFileUpload(newFile);
        toast.success("File uploaded successfully!", {
          description: file.name,
        });
      };
      reader.readAsDataURL(file);
    });

    // Reset input
    e.target.value = "";
  };

  const handleCameraCapture = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFiles = e.target.files;
    if (!selectedFiles) return;

    const filesToAdd = Array.from(selectedFiles);
    
    // Check if adding these files would exceed the limit
    if (!checkFileLimit(filesToAdd.length)) {
      e.target.value = "";
      return;
    }

    filesToAdd.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        const newFile: UploadedFile = {
          id: Date.now().toString() + Math.random().toString(),
          name: file.name || `Photo_${new Date().getTime()}.jpg`,
          type: file.type.startsWith('image/') ? 'image' : 'document',
          url: event.target?.result as string,
          size: formatFileSize(file.size),
          uploadDate: new Date().toISOString(),
        };
        onFileUpload(newFile);
        toast.success("Photo captured successfully!");
      };
      reader.readAsDataURL(file);
    });

    // Reset input
    e.target.value = "";
  };

  const confirmDelete = (id: string) => {
    setDeleteFileId(id);
  };

  const handleDelete = () => {
    if (deleteFileId) {
      onDeleteFile(deleteFileId);
      toast.success("File deleted successfully");
      setDeleteFileId(null);
    }
  };

  const handleCancelDelete = () => {
    setDeleteFileId(null);
    toast.info("Delete cancelled");
  };

  const getFileIcon = (type: string) => {
    if (type.startsWith("image/")) {
      return <ImageIcon className="h-6 w-6" />;
    }
    return <FileText className="h-6 w-6" />;
  };

  const saveAllRecords = () => {
    if (uploadedFiles.length === 0) {
      toast.error("No files to save");
      return;
    }

    // In production, this would upload to Supabase or backend
    toast.success("Records saved successfully!", {
      description: `${uploadedFiles.length} file(s) saved`,
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Toaster position="top-center" />

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={deleteFileId !== null} onOpenChange={(open) => !open && setDeleteFileId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure you want to delete?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the file from your records.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={handleCancelDelete}>No, Keep It</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-red-600 hover:bg-red-700">
              Yes, Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Upgrade Confirmation Dialog */}
      <AlertDialog open={showUpgradeDialog} onOpenChange={(open) => !open && setShowUpgradeDialog(false)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Upgrade Required</AlertDialogTitle>
            <AlertDialogDescription>
              You have reached the file limit for the free plan. Upgrade to a premium plan to upload more files.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setShowUpgradeDialog(false)}>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={() => setShowUpgradeDialog(false)} className="bg-blue-600 hover:bg-blue-700">
              Upgrade
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
              <h1 className="text-gray-900">Upload Medical Records</h1>
              <p className="text-gray-600">Add photos and files securely</p>
            </div>
            {uploadedFiles.length > 0 && (
              <Button
                onClick={saveAllRecords}
                className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
              >
                <Download className="h-5 w-5 mr-2" />
                Save All
              </Button>
            )}
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6 pb-24">
        
        {/* Record Title */}
        <Card className="mb-6 border-blue-200 shadow-sm">
          <CardContent className="p-5">
            <div className="space-y-2">
              <Label htmlFor="recordTitle">Record Title (Optional)</Label>
              <Input
                id="recordTitle"
                placeholder="e.g., Blood Test Results - Nov 2024"
                value={recordTitle}
                onChange={(e) => setRecordTitle(e.target.value)}
                className="border-gray-300"
              />
            </div>
          </CardContent>
        </Card>

        {/* Upload Options */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          {/* Upload Files */}
          <Card className="border-purple-200 bg-gradient-to-br from-purple-50 to-pink-50 shadow-sm hover:shadow-md transition-all cursor-pointer group">
            <CardContent
              className="p-6"
              onClick={() => fileInputRef.current?.click()}
            >
              <div className="text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-purple-100 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                  <Upload className="h-8 w-8 text-purple-600" />
                </div>
                <h3 className="text-gray-900 mb-2">Upload Files</h3>
                <p className="text-gray-600">Choose from device</p>
              </div>
            </CardContent>
          </Card>
          <input
            ref={fileInputRef}
            type="file"
            accept="image/*,.pdf,.doc,.docx"
            multiple
            onChange={handleFileUpload}
            className="hidden"
          />

          {/* Take Photo */}
          <Card className="border-blue-200 bg-gradient-to-br from-blue-50 to-cyan-50 shadow-sm hover:shadow-md transition-all cursor-pointer group">
            <CardContent
              className="p-6"
              onClick={() => cameraInputRef.current?.click()}
            >
              <div className="text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-blue-100 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
                  <Camera className="h-8 w-8 text-blue-600" />
                </div>
                <h3 className="text-gray-900 mb-2">Take Photo</h3>
                <p className="text-gray-600">Use camera</p>
              </div>
            </CardContent>
          </Card>
          <input
            ref={cameraInputRef}
            type="file"
            accept="image/*"
            capture="environment"
            onChange={handleCameraCapture}
            className="hidden"
          />

          {/* Info Card */}
          <Card className="border-emerald-200 bg-gradient-to-br from-emerald-50 to-teal-50 shadow-sm">
            <CardContent className="p-6">
              <div className="text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-emerald-100 rounded-xl flex items-center justify-center">
                  <Eye className="h-8 w-8 text-emerald-600" />
                </div>
                <h3 className="text-gray-900 mb-2">Secure Storage</h3>
                <p className="text-gray-600">Encrypted & private</p>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Info Banner */}
        <Card className="mb-6 border-blue-200 bg-gradient-to-br from-blue-50 to-indigo-50">
          <CardContent className="p-5">
            <div className="flex items-start gap-4">
              <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
                <FileText className="h-6 w-6 text-blue-600" />
              </div>
              <div className="flex-1">
                <h3 className="text-gray-900 mb-2">Supported Files</h3>
                <p className="text-gray-700">
                  You can upload images (JPG, PNG), PDFs, and documents (DOC, DOCX). Maximum file size: 10MB per file.
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* File Limit Indicator */}
        <Card className={`mb-6 ${
          uploadedFiles.length >= MAX_FREE_FILES 
            ? "border-red-200 bg-gradient-to-br from-red-50 to-pink-50" 
            : uploadedFiles.length >= MAX_FREE_FILES - 2 
            ? "border-amber-200 bg-gradient-to-br from-amber-50 to-orange-50"
            : "border-emerald-200 bg-gradient-to-br from-emerald-50 to-teal-50"
        }`}>
          <CardContent className="p-5">
            <div className="flex items-start gap-4">
              <div className={`w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0 ${
                uploadedFiles.length >= MAX_FREE_FILES 
                  ? "bg-red-100" 
                  : uploadedFiles.length >= MAX_FREE_FILES - 2 
                  ? "bg-amber-100"
                  : "bg-emerald-100"
              }`}>
                {uploadedFiles.length >= MAX_FREE_FILES ? (
                  <Crown className="h-6 w-6 text-red-600" />
                ) : uploadedFiles.length >= MAX_FREE_FILES - 2 ? (
                  <AlertCircle className="h-6 w-6 text-amber-600" />
                ) : (
                  <CheckCircle className="h-6 w-6 text-emerald-600" />
                )}
              </div>
              <div className="flex-1">
                <div className="flex items-center justify-between mb-2">
                  <h3 className="text-gray-900">
                    {uploadedFiles.length >= MAX_FREE_FILES 
                      ? "Limit Reached" 
                      : `Free Plan: ${uploadedFiles.length}/${MAX_FREE_FILES} Files Used`}
                  </h3>
                  <Badge className={
                    uploadedFiles.length >= MAX_FREE_FILES 
                      ? "bg-red-100 text-red-800" 
                      : uploadedFiles.length >= MAX_FREE_FILES - 2 
                      ? "bg-amber-100 text-amber-800"
                      : "bg-emerald-100 text-emerald-800"
                  }>
                    {MAX_FREE_FILES - uploadedFiles.length} left
                  </Badge>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2 mb-2">
                  <div 
                    className={`h-2 rounded-full transition-all ${
                      uploadedFiles.length >= MAX_FREE_FILES 
                        ? "bg-red-500" 
                        : uploadedFiles.length >= MAX_FREE_FILES - 2 
                        ? "bg-amber-500"
                        : "bg-emerald-500"
                    }`}
                    style={{ width: `${(uploadedFiles.length / MAX_FREE_FILES) * 100}%` }}
                  ></div>
                </div>
                <p className="text-gray-700">
                  {uploadedFiles.length >= MAX_FREE_FILES 
                    ? "Upgrade to premium to upload unlimited files" 
                    : uploadedFiles.length >= MAX_FREE_FILES - 2
                    ? "You're almost at your limit. Upgrade for unlimited uploads."
                    : "Upload more medical records to keep all your health data organized."}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Uploaded Files List */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-gray-900">Uploaded Files ({uploadedFiles.length})</h2>
            {uploadedFiles.length > 0 && (
              <Badge className="bg-blue-100 text-blue-800">
                {uploadedFiles.length} file{uploadedFiles.length !== 1 ? "s" : ""}
              </Badge>
            )}
          </div>

          {uploadedFiles.length === 0 ? (
            <Card className="border-gray-200">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
                  <Upload className="h-8 w-8 text-gray-400" />
                </div>
                <p className="text-gray-600 mb-2">No files uploaded yet</p>
                <p className="text-gray-500">Upload files or take a photo to get started</p>
              </CardContent>
            </Card>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {uploadedFiles.map((file) => (
                <Card
                  key={file.id}
                  className="border-gray-200 shadow-sm hover:shadow-md transition-all"
                >
                  <CardContent className="p-4">
                    {file.type.startsWith("image/") ? (
                      <div className="mb-4 rounded-lg overflow-hidden bg-gray-100">
                        <img
                          src={file.url}
                          alt={file.name}
                          className="w-full h-48 object-cover"
                        />
                      </div>
                    ) : (
                      <div className="mb-4 h-48 rounded-lg bg-gradient-to-br from-gray-100 to-gray-50 flex items-center justify-center">
                        {getFileIcon(file.type)}
                      </div>
                    )}

                    <div className="space-y-2">
                      <div className="flex items-start justify-between gap-2">
                        <div className="flex-1 min-w-0">
                          <h3 className="text-gray-900 truncate">{file.name}</h3>
                          <p className="text-gray-600">
                            {file.size}
                          </p>
                        </div>
                        <Badge variant="secondary" className="bg-green-100 text-green-800">
                          Uploaded
                        </Badge>
                      </div>

                      <p className="text-gray-500">
                        {new Date(file.uploadDate).toLocaleDateString()} at{" "}
                        {new Date(file.uploadDate).toLocaleTimeString()}
                      </p>

                      <div className="flex gap-2 pt-2">
                        <Button
                          variant="outline"
                          size="sm"
                          className="flex-1 text-blue-600 hover:text-blue-700 hover:bg-blue-50"
                          onClick={() => {
                            const link = document.createElement("a");
                            link.href = file.url;
                            link.download = file.name;
                            link.click();
                            toast.success("File downloaded");
                          }}
                        >
                          <Download className="h-4 w-4 mr-1" />
                          Download
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => confirmDelete(file.id)}
                          className="text-red-600 hover:text-red-700 hover:bg-red-50"
                        >
                          <Trash2 className="h-4 w-4 mr-1" />
                          Delete
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>

        {/* Tips Card */}
        {uploadedFiles.length > 0 && (
          <Card className="mt-6 border-amber-200 bg-gradient-to-br from-amber-50 to-orange-50">
            <CardContent className="p-5">
              <div className="flex items-start gap-4">
                <div className="flex-1">
                  <h3 className="text-gray-900 mb-2">💡 Pro Tip</h3>
                  <p className="text-gray-700">
                    Make sure your medical records are clear and readable. You can add multiple files before saving.
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