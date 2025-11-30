package com.healthapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.healthapp.components.*
import com.healthapp.ui.theme.*
import com.healthapp.viewmodel.HealthAppViewModel

@Composable
fun JaguarHomeScreen(navController: NavController) {
    val viewModel: HealthAppViewModel = viewModel()
    val uploadedFiles by viewModel.uploadedFiles.collectAsState()
    val vaccinations by viewModel.vaccinations.collectAsState()

    val recentActivity = remember(uploadedFiles, vaccinations) {
        (uploadedFiles.map { 
            ActivityItem("File Upload", it.name, it.uploadDate, Icons.Default.UploadFile) 
        } + vaccinations.map { 
            ActivityItem("Vaccination", it.vaccineName, it.dateAdministered, Icons.Default.Bloodtype) 
        }).sortedByDescending { it.date }.take(5)
    }

    JaguarScaffold(
        topBar = { JaguarHeader() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Stats Section
            item {
                JaguarSectionHeader("Health Overview")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    JaguarStatsCard(
                        title = "Health Score",
                        value = "98%",
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )
                    JaguarStatsCard(
                        title = "Appointments",
                        value = "2",
                        icon = Icons.Default.CalendarToday,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Actions
            item {
                JaguarSectionHeader("Quick Actions")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        JaguarActionButton("Upload", Icons.Default.UploadFile) {
                            navController.navigate("upload-record")
                        }
                    }
                    item {
                        JaguarActionButton("Reminder", Icons.Default.Alarm) {
                            navController.navigate("medicine-reminder")
                        }
                    }
                    item {
                        JaguarActionButton("Vaccine", Icons.Default.Bloodtype) {
                            navController.navigate("vaccination-record")
                        }
                    }
                    item {
                        JaguarActionButton("Doctors", Icons.Default.MedicalServices) {
                            navController.navigate("doctors-appointment")
                        }
                    }
                }
            }

            // Recent Activity
            item {
                JaguarSectionHeader("Recent Activity")
                if (recentActivity.isEmpty()) {
                    Text(
                        "No recent activity",
                        color = JaguarTextSecondary,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        recentActivity.forEach { activity ->
                            JaguarActivityRow(activity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JaguarHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello, User",
                style = MaterialTheme.typography.headlineMedium,
                color = JaguarGold,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium,
                color = JaguarTextSecondary
            )
        }
        // Profile Icon Placeholder
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = JaguarGoldDim,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, tint = JaguarBlack)
            }
        }
    }
}

@Composable
fun JaguarStatsCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    JaguarCard(modifier = modifier) {
        Icon(icon, contentDescription = null, tint = JaguarGold, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(value, style = MaterialTheme.typography.headlineLarge, color = JaguarTextPrimary, fontWeight = FontWeight.Bold)
        Text(title, style = MaterialTheme.typography.bodyMedium, color = JaguarTextSecondary)
    }
}

@Composable
fun JaguarActionButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = JaguarCharcoal),
            modifier = Modifier.size(72.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(icon, contentDescription = null, tint = JaguarAccent, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = JaguarTextPrimary)
    }
}

@Composable
fun JaguarActivityRow(item: ActivityItem) {
    JaguarCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(item.icon, contentDescription = null, tint = JaguarGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(item.title, style = MaterialTheme.typography.titleMedium, color = JaguarTextPrimary)
                Text(item.subtitle, style = MaterialTheme.typography.bodySmall, color = JaguarTextSecondary)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(item.date, style = MaterialTheme.typography.labelSmall, color = JaguarTextSecondary)
        }
    }
}

data class ActivityItem(
    val title: String,
    val subtitle: String,
    val date: String,
    val icon: ImageVector
)
