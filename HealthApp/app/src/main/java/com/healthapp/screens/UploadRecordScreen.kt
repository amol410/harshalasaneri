package com.healthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.ui.theme.HealthAppTheme
import java.text.SimpleDateFormat
import java.util.*

data class UploadedFile(
    val id: String,
    val name: String,
    val type: String, // "image" or "document"
    val uploadDate: String,
    val size: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadRecordScreen(
    navController: NavController,
    onFileUpload: (UploadedFile) -> Unit,
    uploadedFiles: List<UploadedFile>,
    onDeleteFile: (String) -> Unit
) {
    var recordTitle by remember { mutableStateOf("") }
    var showUpgradeDialog by remember { mutableStateOf(false) }
    var deleteFileId by remember { mutableStateOf<String?>(null) }
    val MAX_FREE_FILES = 10

    // Format file size
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${String.format("%.1f", bytes / 1024.0)} KB"
            else -> "${String.format("%.1f", bytes / (1024.0 * 1024.0))} MB"
        }
    }

    // Check file limit
    fun checkFileLimit(count: Int = 1): Boolean {
        if (uploadedFiles.size + count > MAX_FREE_FILES) {
            showUpgradeDialog = true
            return false
        }
        return true
    }

    // Handle file upload (mock implementation)
    fun handleFileUpload(fileName: String, type: String) {
        if (!checkFileLimit(1)) return

        val newFile = UploadedFile(
            id = System.currentTimeMillis().toString() + (0..1000).random().toString(),
            name = fileName,
            type = type,
            url = "", // In real implementation, this would be the file path
            size = "${(100 + (0..5000).random()).toLong()} KB", // Mock size
            uploadDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
        )
        onFileUpload(newFile)
    }

    // Delete file
    fun confirmDelete(id: String) {
        deleteFileId = id
    }

    // Format date for display
    fun formatDate(dateString: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = format.parse(dateString)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
        return outputFormat.format(date ?: Date())
    }

    // Get file icon
    fun getFileIcon(type: String) = when {
        type.startsWith("image/") || type == "image" -> Icons.Default.Image
        else -> Icons.Default.Description
    }

    // Get file color
    @Composable
    fun getFileColor(type: String) = when (type) {
        "image" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Upload Medical Records", fontWeight = FontWeight.Bold)
                        Text("Add photos and files securely", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uploadedFiles.isNotEmpty()) {
                        IconButton(onClick = { /* Save all records */ }) {
                            Icon(Icons.Default.Download, contentDescription = "Save All")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Record Title
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                OutlinedTextField(
                    value = recordTitle,
                    onValueChange = { recordTitle = it },
                    label = { Text("Record Title (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("e.g., Blood Test Results - Nov 2024") }
                )
            }

            // Upload Options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Upload Files Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { handleFileUpload("Sample_Document.pdf", "document") },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Upload Files", fontWeight = FontWeight.Medium)
                        Text("Choose from device", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Take Photo Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { handleFileUpload("Photo_${System.currentTimeMillis()}.jpg", "image") },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Take Photo", fontWeight = FontWeight.Medium)
                        Text("Use camera", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Secure Storage",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Encrypted & private",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Info Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Supported Files",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "You can upload images (JPG, PNG), PDFs, and documents (DOC, DOCX). Maximum file size: 10MB per file.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // File Limit Indicator
            val fileLimitColor = when {
                uploadedFiles.size >= MAX_FREE_FILES -> MaterialTheme.colorScheme.errorContainer
                uploadedFiles.size >= MAX_FREE_FILES - 2 -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.primaryContainer
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = fileLimitColor.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                fileLimitColor,
                                MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            when {
                                uploadedFiles.size >= MAX_FREE_FILES -> Icons.Default.Star
                                uploadedFiles.size >= MAX_FREE_FILES - 2 -> Icons.Default.Warning
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = when {
                                uploadedFiles.size >= MAX_FREE_FILES -> MaterialTheme.colorScheme.onErrorContainer
                                uploadedFiles.size >= MAX_FREE_FILES - 2 -> MaterialTheme.colorScheme.onSecondaryContainer
                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when {
                                    uploadedFiles.size >= MAX_FREE_FILES -> "Limit Reached"
                                    else -> "Free Plan: ${uploadedFiles.size}/$MAX_FREE_FILES Files Used"
                                },
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Badge(
                                containerColor = when {
                                    uploadedFiles.size >= MAX_FREE_FILES -> MaterialTheme.colorScheme.errorContainer
                                    uploadedFiles.size >= MAX_FREE_FILES - 2 -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                },
                                contentColor = when {
                                    uploadedFiles.size >= MAX_FREE_FILES -> MaterialTheme.colorScheme.onErrorContainer
                                    uploadedFiles.size >= MAX_FREE_FILES - 2 -> MaterialTheme.colorScheme.onSecondaryContainer
                                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                                }
                            ) {
                                Text("${MAX_FREE_FILES - uploadedFiles.size} left")
                            }
                        }
                        LinearProgressIndicator(
                            progress = (uploadedFiles.size.toFloat() / MAX_FREE_FILES).coerceIn(0f, 1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = when {
                                uploadedFiles.size >= MAX_FREE_FILES -> MaterialTheme.colorScheme.error
                                uploadedFiles.size >= MAX_FREE_FILES - 2 -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                        Text(
                            text = when {
                                uploadedFiles.size >= MAX_FREE_FILES -> "Upgrade to premium to upload unlimited files"
                                uploadedFiles.size >= MAX_FREE_FILES - 2 -> "You're almost at your limit. Upgrade for unlimited uploads."
                                else -> "Upload more medical records to keep all your health data organized."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Uploaded Files List
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Uploaded Files (${uploadedFiles.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (uploadedFiles.isNotEmpty()) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text("${uploadedFiles.size} file${if (uploadedFiles.size != 1) "s" else ""}")
                    }
                }
            }

            if (uploadedFiles.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.shapes.small
                                )
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "No files uploaded yet",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Upload files or take a photo to get started",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uploadedFiles) { file ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                // File preview
                                when (file.type) {
                                    "image" -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(192.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.LightGray)
                                        ) {
                                            // In a real app, you would load the image here
                                            Icon(
                                                Icons.Default.Image,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                    else -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(192.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .padding(32.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                getFileIcon(file.type),
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp)
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = file.name,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = file.size,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ) {
                                        Text("Uploaded")
                                    }
                                }

                                Text(
                                    text = formatDate(file.uploadDate),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { /* Download file */ },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.outline
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "Download",
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                    Button(
                                        onClick = { confirmDelete(file.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "Delete",
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tips Card
            if (uploadedFiles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "💡 Pro Tip",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Make sure your medical records are clear and readable. You can add multiple files before saving.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (deleteFileId != null) {
        AlertDialog(
            onDismissRequest = { deleteFileId = null },
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this record? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteFileId?.let { id ->
                            onDeleteFile(id)
                            deleteFileId = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteFileId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Upgrade confirmation dialog
    if (showUpgradeDialog) {
        AlertDialog(
            onDismissRequest = { showUpgradeDialog = false },
            title = { Text("Upgrade Required") },
            text = { Text("You have reached the file limit for the free plan. Upgrade to a premium plan to upload more files.") },
            confirmButton = {
                Button(
                    onClick = { showUpgradeDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Upgrade")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showUpgradeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}