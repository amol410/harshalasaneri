package com.healthapp.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.model.HealthRecord
import com.healthapp.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(navController: NavController) {

    var selectedItem by remember { mutableStateOf(1) }
    val items = listOf("Home", "Profile")

    val records = remember { mutableStateListOf<HealthRecord>() }

    // Dialog states
    var showUploadDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Selected record for deletion
    var selectedRecord by remember { mutableStateOf<HealthRecord?>(null) }

    var docTitle by remember { mutableStateOf("") }
    var docCategory by remember { mutableStateOf("General") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // File picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedFileUri = it
            showConfirmDialog = true
        }
    }

    // Camera preview launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selectedFileUri = Uri.parse("camera_photo_${System.currentTimeMillis()}")
            showConfirmDialog = true
        }
    }

    // ========== UPLOAD DIALOG ==========
    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Add Health Document") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    OutlinedTextField(
                        value = docTitle,
                        onValueChange = { docTitle = it },
                        label = { Text("Document Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Choose Category:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = { docCategory = "Prescription" },
                            label = { Text("Prescription") }
                        )
                        AssistChip(
                            onClick = { docCategory = "Lab Report" },
                            label = { Text("Lab Report") }
                        )
                        AssistChip(
                            onClick = { docCategory = "General" },
                            label = { Text("General") }
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = { filePickerLauncher.launch("*/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Upload File")
                    }

                    Button(
                        onClick = { cameraLauncher.launch(null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Take Photo")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) { Text("Close") }
            },
            confirmButton = {}
        )
    }

    // ========== CONFIRM SAVE ==========
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Save Document?") },
            text = { Text("Do you want to save this document?") },
            confirmButton = {
                TextButton(onClick = {
                    if (docTitle.isNotBlank() && selectedFileUri != null) {
                        records.add(
                            HealthRecord(
                                id = (records.size + 1).toString(),
                                title = docTitle,
                                date = "2023-11-22",
                                category = docCategory,
                                filePath = selectedFileUri.toString()
                            )
                        )
                        docTitle = ""
                        selectedFileUri = null
                        showConfirmDialog = false
                        showUploadDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    docTitle = ""
                    selectedFileUri = null
                    showConfirmDialog = false
                    showUploadDialog = false
                }) {
                    Text("Delete")
                }
            }
        )
    }

    // ========== DELETE CONFIRMATION ==========
    if (showDeleteDialog && selectedRecord != null) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Delete Document") },
            text = { Text("You want to delete the data?") },
            confirmButton = {
                TextButton(onClick = {
                    records.remove(selectedRecord)
                    selectedRecord = null
                    showDeleteDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedRecord = null
                    showDeleteDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }

    // ========== MAIN UI ==========
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Records") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showUploadDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Document")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = items,
                selectedItem = selectedItem,
                onItemClick = { index ->
                    selectedItem = index
                    when (index) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("profile")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("My Documents", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(records) { record ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = when (record.category) {
                                    "Prescription" -> Icons.Filled.MedicalInformation
                                    "Lab Report" -> Icons.Filled.Science
                                    else -> Icons.Filled.Description
                                },
                                contentDescription = null
                            )

                            Spacer(Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(record.title, fontWeight = FontWeight.Bold)
                                Text("${record.category} • ${record.date}",
                                    style = MaterialTheme.typography.bodySmall)
                            }

                            // VIEW BUTTON → Opens ViewerScreen
                            IconButton(onClick = {
                                navController.navigate("viewRecord/${record.filePath}")
                            }) {
                                Icon(Icons.Filled.Visibility, contentDescription = "View")
                            }

                            // DELETE BUTTON
                            IconButton(onClick = {
                                selectedRecord = record
                                showDeleteDialog = true
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
