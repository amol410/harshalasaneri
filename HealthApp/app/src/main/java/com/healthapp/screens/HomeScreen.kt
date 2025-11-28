package com.healthapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.healthapp.components.BottomNavigationBar
import com.healthapp.viewmodel.HealthAppViewModel
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.collectAsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HealthAppViewModel = viewModel()

    // Get recent activity from ViewModel
    val uploadedFiles by viewModel.uploadedFiles.collectAsState()
    val vaccinations by viewModel.vaccinations.collectAsState()

    // Get recent activity (last 2 items)
    val recentVaccinations = remember(vaccinations) {
        vaccinations.takeLast(2).reversed()
    }

    val recentUploads = remember(uploadedFiles) {
        uploadedFiles.takeLast(2).reversed()
    }

    // Convert to display format
    val recentVaccinationItems = recentVaccinations.map { vaccination ->
        VaccinationItem(
            vaccination.id,
            vaccination.vaccineName,
            vaccination.doseNumber,
            vaccination.dateAdministered,
            vaccination.createdAt
        )
    }

    val recentUploadFileItems = recentUploads.map { file ->
        UploadFileItem(
            file.id,
            file.name,
            file.type,
            file.uploadDate,
            file.size,
            file.url
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Welcome, userName", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("How are you feeling today?", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            Badge { }
                        }
                    ) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = listOf("Home", "Appointment", "History"),
                selectedItem = 0,
                onItemClick = { index ->
                    when (index) {
                        0 -> {} // Already on home screen
                        1 -> navController.navigate("doctors-appointment")    // Navigate to appointment
                        2 -> navController.navigate("history")                // Navigate to history
                    }
                }
            )
        }
    ) { padding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Quick Actions
            Text("Quick Actions", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionItem(
                    icon = Icons.Default.EmojiObjects,
                    label = "Set Reminder",
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.surface
                    ),
                    onClick = { navController.navigate("medicine-reminder") }
                )
                QuickActionItem(
                    icon = Icons.Default.UploadFile,
                    label = "Upload Record",
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.surface
                    ),
                    onClick = { navController.navigate("upload-record") }
                )
                QuickActionItem(
                    icon = Icons.Default.Bloodtype,
                    label = "Vaccination",
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.surface
                    ),
                    onClick = { navController.navigate("vaccination-record") }
                )
                QuickActionItem(
                    icon = Icons.Default.Person,
                    label = "Update Profile",
                    gradientColors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surface
                    ),
                    onClick = { navController.navigate("profile") }
                )
            }

            // Recommendation Card
            Text("Recommendation", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("For illness this days", fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text("Health Alert") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Based on current health trends, make sure to get adequate rest and maintain good hygiene practices.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Healthy Health Benefits Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Healthy Health Benefits",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    BenefitItem(text = "Regular exercise improves cardiovascular health")
                    BenefitItem(text = "Balanced diet boosts immune system")
                    BenefitItem(text = "Adequate sleep enhances mental clarity")
                }
            }

            // Recent Activity
            Text("Recent Activity", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Recent Vaccinations
                recentVaccinationItems.forEach { vaccination ->
                    ActivityCard(
                        icon = Icons.Default.Bloodtype,
                        title = "Vaccination",
                        subtitle = "${vaccination.dateAdministered} • ${vaccination.vaccineName}",
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }

                // Recent Uploads
                recentUploadFileItems.forEach { file ->
                    val icon = if (file.type == "image") Icons.Default.Image else Icons.Default.Description
                    ActivityCard(
                        icon = icon,
                        title = "New Record Added",
                        subtitle = "${formatDate(file.uploadDate)} • ${file.name}",
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                // Empty State
                if (recentVaccinationItems.isEmpty() && recentUploadFileItems.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                "No Recent Activity",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Your recent vaccinations and uploaded records will appear here",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Daily Health Tip
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Daily Health Tip", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Stay hydrated! Drink at least 8 glasses of water today.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


// -------------------- Helper Components --------------------
@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            label,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun BenefitItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActivityCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .padding(12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Helper function to format dates
fun formatDate(dateString: String): String {
    return try {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val now = Date()
        val diffTime = abs(now.time - date?.time!!)
        val diffDays = ceil(diffTime.toDouble() / (1000 * 60 * 60 * 24)).toInt()

        when {
            diffDays == 0 -> "Today"
            diffDays == 1 -> "Yesterday"
            diffDays < 7 -> "$diffDays days ago"
            else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        dateString
    }
}

// Data classes for mock data
data class VaccinationItem(
    val id: String,
    val vaccineName: String,
    val doseNumber: String,
    val dateAdministered: String,
    val createdAt: String
)

data class UploadFileItem(
    val id: String,
    val name: String,
    val type: String,
    val uploadDate: String,
    val size: String,
    val url: String
)
