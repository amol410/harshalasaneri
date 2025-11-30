package com.healthapp.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.components.*
import com.healthapp.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    
    // Mock Data
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@example.com") }
    var phone by remember { mutableStateOf("+1 234 567 8900") }

    JaguarScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = JaguarGold)
                }
                Text(
                    "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    color = JaguarTextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = JaguarGold,
                    shape = CircleShape,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        "Premium",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = JaguarBlack,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(JaguarGoldDim)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(JaguarCharcoal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = JaguarTextSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                }
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(JaguarGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = JaguarBlack, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(name, style = MaterialTheme.typography.headlineMedium, color = JaguarTextPrimary, fontWeight = FontWeight.Bold)
            Text(email, style = MaterialTheme.typography.bodyMedium, color = JaguarTextSecondary)

            Spacer(modifier = Modifier.height(32.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                JaguarStatsCard(title = "Age", value = "32", icon = Icons.Default.Cake, modifier = Modifier.weight(1f))
                JaguarStatsCard(title = "Blood", value = "A+", icon = Icons.Default.Bloodtype, modifier = Modifier.weight(1f))
                JaguarStatsCard(title = "Records", value = "12", icon = Icons.Default.Description, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Details Section
            JaguarSectionHeader("Personal Details")
            
            JaguarCard(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailItem("Full Name", name)
                Divider(color = JaguarDarkGray, modifier = Modifier.padding(vertical = 12.dp))
                ProfileDetailItem("Email", email)
                Divider(color = JaguarDarkGray, modifier = Modifier.padding(vertical = 12.dp))
                ProfileDetailItem("Phone", phone)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            JaguarButton(
                text = if (isEditing) "Save Changes" else "Edit Profile",
                onClick = { isEditing = !isEditing },
                modifier = Modifier.fillMaxWidth(),
                icon = if (isEditing) Icons.Default.Save else Icons.Default.Edit
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = JaguarError),
                border = BorderStroke(1.dp, JaguarError)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = JaguarTextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge, color = JaguarTextPrimary)
    }
}