package com.healthapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.ui.theme.HealthAppTheme
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import java.text.SimpleDateFormat
import java.util.*

data class Reminder(
    val id: String,
    val medicineName: String,
    val dosage: String,
    val time: String,
    val phoneNumber: String,
    val active: Boolean,
    val durationType: String, // "everyday", "week", "custom"
    val startDate: String,
    val endDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineReminderScreen(navController: NavController) {
    var reminders by remember { mutableStateOf(emptyList<Reminder>()) }
    var showForm by remember { mutableStateOf(false) }
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var durationType by remember { mutableStateOf("everyday") }
    var startDate by remember { mutableStateOf(java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var endDate by remember { mutableStateOf("") }
    
    // Format date for display
    fun formatDate(dateString: String): String {
        if (dateString.isEmpty()) return ""
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dateString)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return outputFormat.format(date ?: Date())
    }
    
    // Get duration text
    fun getDurationText(reminder: Reminder): String {
        return when (reminder.durationType) {
            "everyday" -> "Everyday (Ongoing)"
            "week" -> "For One Week (until ${formatDate(reminder.endDate)})"
            else -> "${formatDate(reminder.startDate)} - ${formatDate(reminder.endDate)}"
        }
    }
    
    // Submit reminder
    fun submitReminder() {
        if (medicineName.isEmpty() || time.isEmpty() || phoneNumber.isEmpty()) {
            // Show error message
            return
        }
        
        val calculatedEndDate = when (durationType) {
            "week" -> {
                val start = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate)
                val cal = Calendar.getInstance()
                cal.time = start ?: Date()
                cal.add(Calendar.DAY_OF_MONTH, 7)
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
            }
            "everyday" -> ""
            else -> endDate
        }
        
        val newReminder = Reminder(
            id = System.currentTimeMillis().toString(),
            medicineName = medicineName,
            dosage = if (dosage.isEmpty()) "As prescribed" else dosage,
            time = time,
            phoneNumber = phoneNumber,
            active = true,
            durationType = durationType,
            startDate = startDate,
            endDate = calculatedEndDate
        )
        
        reminders = reminders + newReminder
        // Reset form
        medicineName = ""
        dosage = ""
        time = ""
        phoneNumber = ""
        durationType = "everyday"
        startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        endDate = ""
        showForm = false
    }
    
    // Delete reminder
    fun deleteReminder(id: String) {
        reminders = reminders.filter { it.id != id }
    }
    
    // Toggle reminder
    fun toggleReminder(id: String) {
        reminders = reminders.map { 
            if (it.id == id) it.copy(active = !it.active) else it 
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Medicine Reminders", fontWeight = FontWeight.Bold)
                        Text("Never miss your medication", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showForm = !showForm }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Reminder")
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
            // Add Reminder Form
            AnimatedVisibility(visible = showForm) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Add Medicine Reminder",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        OutlinedTextField(
                            value = medicineName,
                            onValueChange = { medicineName = it },
                            label = { Text("Medicine Name *") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = dosage,
                            onValueChange = { dosage = it },
                            label = { Text("Dosage") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            label = { Text("Time *") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) }
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number (for SMS) *") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Duration Type
                        Text("Duration Type *", modifier = Modifier.padding(bottom = 4.dp))
                        
                        // Radio buttons for duration
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { durationType = "everyday" }
                            ) {
                                RadioButton(
                                    selected = durationType == "everyday",
                                    onClick = { durationType = "everyday" }
                                )
                                Text("Everyday (Ongoing)")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { durationType = "week" }
                            ) {
                                RadioButton(
                                    selected = durationType == "week",
                                    onClick = { durationType = "week" }
                                )
                                Text("For One Week")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { durationType = "custom" }
                            ) {
                                RadioButton(
                                    selected = durationType == "custom",
                                    onClick = { durationType = "custom" }
                                )
                                Text("Custom Time Period")
                            }
                        }
                        
                        // Start date for custom
                        if (durationType == "custom") {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = startDate,
                                onValueChange = { startDate = it },
                                label = { Text("Start Date") },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
                            )
                        }
                        
                        // End date for custom
                        if (durationType == "custom") {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = endDate,
                                onValueChange = { endDate = it },
                                label = { Text("End Date") },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { showForm = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text("Cancel")
                            }
                            
                            Button(
                                onClick = { submitReminder() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Set Reminder")
                            }
                        }
                    }
                }
            }
            
            // Info Card
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
                        Icon(Icons.Default.Message, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "SMS Notifications",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "You'll receive text message reminders at the scheduled times. Make sure to provide a valid phone number.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Reminders List
            Text(
                text = "Your Reminders (${reminders.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (reminders.isEmpty()) {
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
                            Icon(Icons.Default.EmojiObjects, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "No reminders set yet",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Click \"Add New\" to create your first medicine reminder",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(reminders) { reminder ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!reminder.active) 
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) 
                                else 
                                    MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            if (reminder.active)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.EmojiObjects,
                                        contentDescription = null,
                                        tint = if (reminder.active)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = reminder.medicineName,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = reminder.dosage,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        
                                        if (reminder.active) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            ) {
                                                Text("Active")
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = reminder.time,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = reminder.phoneNumber,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = getDurationText(reminder),
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { toggleReminder(reminder.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.outline
                                            ),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Icon(Icons.Default.NotificationImportant, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Text(
                                                text = if (reminder.active) "Disable" else "Enable",
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                        
                                        Button(
                                            onClick = { deleteReminder(reminder.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                                            ),
                                            modifier = Modifier.height(32.dp)
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
            }
            
            // Demo Button for Testing
            if (reminders.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Test Notification",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Click below to test the SMS notification for your reminders",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = { /* Test notification */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                            Text("Send Test SMS")
                        }
                    }
                }
            }
        }
    }
}