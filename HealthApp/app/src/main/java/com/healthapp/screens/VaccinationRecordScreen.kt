package com.healthapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.ui.theme.HealthAppTheme
import java.text.SimpleDateFormat
import java.util.*

data class Vaccination(
    val id: String,
    val vaccineName: String,
    val doseNumber: String,
    val dateAdministered: String,
    val nextDueDate: String,
    val administeredBy: String,
    val location: String,
    val batchNumber: String,
    val notes: String,
    val createdAt: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationRecordScreen(
    navController: NavController,
    onVaccinationAdd: (Vaccination) -> Unit,
    onVaccinationDelete: (String) -> Unit
) {
    var vaccineName by remember { mutableStateOf("") }
    var doseNumber by remember { mutableStateOf("") }
    var dateAdministered by remember { mutableStateOf("") }
    var nextDueDate by remember { mutableStateOf("") }
    var administeredBy by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var batchNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var vaccinations by remember { mutableStateOf(emptyList<Vaccination>()) }
    var deleteVaccinationId by remember { mutableStateOf<String?>(null) }
    
    // Format date for display
    fun formatDate(dateString: String): String {
        if (dateString.isEmpty()) return ""
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dateString)
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return outputFormat.format(date ?: Date())
    }
    
    // Check if vaccination is upcoming (within 30 days)
    fun isUpcoming(dateString: String): Boolean {
        if (dateString.isEmpty()) return false
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val nextDate = format.parse(dateString)
        val today = Date()
        val diffTime = nextDate?.time?.minus(today.time) ?: 0
        val diffDays = (diffTime / (1000 * 60 * 60 * 24)).toInt()
        return diffDays > 0 && diffDays <= 30
    }
    
    // Check if vaccination is overdue
    fun isOverdue(dateString: String): Boolean {
        if (dateString.isEmpty()) return false
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val nextDate = format.parse(dateString)
        val today = Date()
        return nextDate?.before(today) == true
    }
    
    // Add vaccination
    fun addVaccination() {
        if (vaccineName.trim().isEmpty()) {
            // Show error
            return
        }
        if (doseNumber.trim().isEmpty()) {
            // Show error
            return
        }
        if (dateAdministered.isEmpty()) {
            // Show error
            return
        }
        if (administeredBy.trim().isEmpty()) {
            // Show error
            return
        }
        if (location.trim().isEmpty()) {
            // Show error
            return
        }
        
        val newVaccination = Vaccination(
            id = System.currentTimeMillis().toString() + (0..1000).random().toString(),
            vaccineName = vaccineName.trim(),
            doseNumber = doseNumber.trim(),
            dateAdministered = dateAdministered,
            nextDueDate = nextDueDate,
            administeredBy = administeredBy.trim(),
            location = location.trim(),
            batchNumber = batchNumber.trim(),
            notes = notes.trim(),
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
        )
        
        vaccinations = listOf(newVaccination) + vaccinations // Add to beginning of list
        
        // Reset form
        vaccineName = ""
        doseNumber = ""
        dateAdministered = ""
        nextDueDate = ""
        administeredBy = ""
        location = ""
        batchNumber = ""
        notes = ""
        
        onVaccinationAdd(newVaccination)
    }
    
    // Delete vaccination
    fun confirmDeleteVaccination(id: String) {
        deleteVaccinationId = id
    }
    
    fun deleteVaccination() {
        if (deleteVaccinationId != null) {
            val deletedId = deleteVaccinationId!!
            vaccinations = vaccinations.filter { it.id != deletedId }
            onVaccinationDelete(deletedId)
            deleteVaccinationId = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Vaccination Record", fontWeight = FontWeight.Bold)
                        Text("Track your immunization history", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Vaccination Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Add Vaccination Record",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Vaccine Name & Dose Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Vaccine Name
                        OutlinedTextField(
                            value = vaccineName,
                            onValueChange = { vaccineName = it },
                            label = { Text("Vaccine Name") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Dose Number
                        OutlinedTextField(
                            value = doseNumber,
                            onValueChange = { doseNumber = it },
                            label = { Text("Dose Number") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Dates Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Date Administered
                        OutlinedTextField(
                            value = dateAdministered,
                            onValueChange = { dateAdministered = it },
                            label = { Text("Date Administered") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        
                        // Next Due Date
                        OutlinedTextField(
                            value = nextDueDate,
                            onValueChange = { nextDueDate = it },
                            label = { Text("Next Dose Due Date (Optional)") },
                            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Administered By & Location Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Administered By
                        OutlinedTextField(
                            value = administeredBy,
                            onValueChange = { administeredBy = it },
                            label = { Text("Administered By") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Location
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Batch Number
                    OutlinedTextField(
                        value = batchNumber,
                        onValueChange = { batchNumber = it },
                        label = { Text("Batch/Lot Number (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Additional Notes (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }
            }
            
            // Save Button
            Button(
                onClick = { addVaccination() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Save Vaccination Record")
            }
            
            // Vaccination Records
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vaccination History (${vaccinations.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (vaccinations.isNotEmpty()) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Text("${vaccinations.size} record${if (vaccinations.size != 1) "s" else ""}")
                    }
                }
            }
            
            if (vaccinations.isEmpty()) {
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
                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "No vaccination records yet",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Add your first vaccination record above",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(vaccinations) { vaccination ->
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
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.tertiaryContainer,
                                                    MaterialTheme.shapes.small
                                                )
                                                .padding(12.dp)
                                        ) {
                                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = vaccination.vaccineName,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = vaccination.doseNumber,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    
                                    IconButton(
                                        onClick = { confirmDeleteVaccination(vaccination.id) },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete Vaccination", modifier = Modifier.size(16.dp))
                                    }
                                }
                                
                                // Date and Details
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Administered: ${formatDate(vaccination.dateAdministered)}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                if (vaccination.nextDueDate.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                                    ) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Next Due: ${formatDate(vaccination.nextDueDate)}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isOverdue(vaccination.nextDueDate)) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                                            ) {
                                                Text("Overdue", fontSize = 10.sp)
                                            }
                                        } else if (isUpcoming(vaccination.nextDueDate)) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                            ) {
                                                Text("Upcoming", fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "By: ${vaccination.administeredBy}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "At: ${vaccination.location}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                if (vaccination.batchNumber.isNotEmpty()) {
                                    Text(
                                        text = "Batch: ${vaccination.batchNumber}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(start = 60.dp, bottom = 4.dp)
                                    )
                                }
                                
                                // Notes
                                if (vaccination.notes.isNotEmpty()) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 60.dp, top = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Text(
                                            text = vaccination.notes,
                                            modifier = Modifier.padding(8.dp),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Info Card
            if (vaccinations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "💉 Vaccination Tip",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Keep your vaccination records up to date. Set reminders for booster shots and follow-up doses. Always consult with your healthcare provider about recommended vaccinations.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (deleteVaccinationId != null) {
        AlertDialog(
            onDismissRequest = { deleteVaccinationId = null },
            title = { Text("Delete Vaccination Record?") },
            text = { Text("Are you sure you want to delete this vaccination record? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { deleteVaccination(); deleteVaccinationId = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteVaccinationId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}