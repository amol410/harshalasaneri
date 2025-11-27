import { useState } from "react";
import { ArrowLeft, FileText, Image as ImageIcon, Calendar, Download, Trash2, Search, Filter } from "lucide-react";
import { Button } from "./ui/button";
import { Card, CardContent } from "./ui/card";
import { Input } from "./ui/input";
import { Badge } from "./ui/badge";
import { motion, AnimatePresence } from "motion/react";
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
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";

interface UploadedFile {
  id: string;
  name: string;
  type: string;
  uploadDate: string;
  size: string;
  url: string;
}

interface HistoryProps {
  onBack: () => void;
  uploadedFiles: UploadedFile[];
  onDeleteFile: (id: string) => void;
}

export function History({ onBack, uploadedFiles, onDeleteFile }: HistoryProps) {
  const [searchQuery, setSearchQuery] = useState("");
  const [filterType, setFilterType] = useState<string>("all");
  const [deleteId, setDeleteId] = useState<string | null>(null);

  // Filter files based on search and type
  const filteredFiles = uploadedFiles.filter((file) => {
    const matchesSearch = file.name.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesType = filterType === "all" || file.type === filterType;
    return matchesSearch && matchesType;
  });

  // Group files by date
  const groupedFiles = filteredFiles.reduce((acc, file) => {
    const date = new Date(file.uploadDate).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
    if (!acc[date]) {
      acc[date] = [];
    }
    acc[date].push(file);
    return acc;
  }, {} as Record<string, UploadedFile[]>);

  const handleDelete = (id: string) => {
    setDeleteId(id);
  };

  const confirmDelete = () => {
    if (deleteId) {
      onDeleteFile(deleteId);
      setDeleteId(null);
    }
  };

  const getFileIcon = (type: string) => {
    if (type === "image") {
      return <ImageIcon className="h-5 w-5" />;
    }
    return <FileText className="h-5 w-5" />;
  };

  const getFileColor = (type: string) => {
    if (type === "image") {
      return "from-purple-100 to-purple-50 text-purple-600 border-purple-200";
    }
    return "from-blue-100 to-blue-50 text-blue-600 border-blue-200";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50 pb-24">
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
              <h1 className="text-gray-900">Medical History</h1>
            </div>
            <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-200">
              {uploadedFiles.length} Records
            </Badge>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-6">
        {/* Search and Filter */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4 }}
          className="mb-6 space-y-3"
        >
          {/* Search Bar */}
          <div className="relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
            <Input
              type="text"
              placeholder="Search records..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-12 h-12 bg-white border-gray-300 focus:border-blue-500 rounded-xl transition-colors"
            />
          </div>

          {/* Filter */}
          <div className="flex items-center gap-3">
            <Filter className="h-5 w-5 text-gray-600" />
            <Select value={filterType} onValueChange={setFilterType}>
              <SelectTrigger className="w-full bg-white border-gray-300 focus:border-blue-500 rounded-xl h-12">
                <SelectValue placeholder="Filter by type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Files</SelectItem>
                <SelectItem value="document">Documents</SelectItem>
                <SelectItem value="image">Images</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </motion.div>

        {/* Files List */}
        {Object.keys(groupedFiles).length === 0 ? (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4, delay: 0.2 }}
            className="text-center py-16"
          >
            <div className="w-20 h-20 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
              <FileText className="h-10 w-10 text-gray-400" />
            </div>
            <h3 className="text-gray-900 mb-2">No Records Found</h3>
            <p className="text-gray-600">
              {searchQuery || filterType !== "all"
                ? "Try adjusting your search or filter"
                : "Upload your first medical record to get started"}
            </p>
          </motion.div>
        ) : (
          <div className="space-y-6">
            {Object.entries(groupedFiles).map(([date, files], groupIndex) => (
              <motion.div
                key={date}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.4, delay: groupIndex * 0.1 }}
              >
                {/* Date Header */}
                <div className="flex items-center gap-3 mb-3">
                  <Calendar className="h-4 w-4 text-gray-500" />
                  <h2 className="text-gray-700">{date}</h2>
                </div>

                {/* Files */}
                <div className="space-y-3">
                  <AnimatePresence>
                    {files.map((file, fileIndex) => (
                      <motion.div
                        key={file.id}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: 20 }}
                        transition={{ duration: 0.3, delay: fileIndex * 0.05 }}
                      >
                        <Card className={`border bg-gradient-to-br ${getFileColor(file.type)} shadow-sm hover:shadow-md transition-all`}>
                          <CardContent className="p-4">
                            <div className="flex items-center gap-4">
                              {/* File Icon/Preview */}
                              <div className="flex-shrink-0">
                                {file.type === "image" ? (
                                  <div className="w-16 h-16 rounded-xl overflow-hidden bg-white shadow-sm">
                                    <img
                                      src={file.url}
                                      alt={file.name}
                                      className="w-full h-full object-cover"
                                    />
                                  </div>
                                ) : (
                                  <div className="w-16 h-16 bg-white rounded-xl flex items-center justify-center shadow-sm">
                                    {getFileIcon(file.type)}
                                  </div>
                                )}
                              </div>

                              {/* File Info */}
                              <div className="flex-1 min-w-0">
                                <h3 className="text-gray-900 truncate mb-1">
                                  {file.name}
                                </h3>
                                <div className="flex items-center gap-2 text-gray-600">
                                  <span className="capitalize">{file.type}</span>
                                  <span>•</span>
                                  <span>{file.size}</span>
                                </div>
                              </div>

                              {/* Actions */}
                              <div className="flex items-center gap-2 flex-shrink-0">
                                <Button
                                  variant="ghost"
                                  size="icon"
                                  className="hover:bg-white/50 transition-colors"
                                  onClick={() => {
                                    // Download file
                                    const link = document.createElement('a');
                                    link.href = file.url;
                                    link.download = file.name;
                                    link.click();
                                  }}
                                >
                                  <Download className="h-4 w-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="icon"
                                  className="hover:bg-red-100 text-red-600 transition-colors"
                                  onClick={() => handleDelete(file.id)}
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </div>
                            </div>
                          </CardContent>
                        </Card>
                      </motion.div>
                    ))}
                  </AnimatePresence>
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </main>

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={deleteId !== null} onOpenChange={() => setDeleteId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Record</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this record? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={confirmDelete}
              className="bg-red-600 hover:bg-red-700 text-white"
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
