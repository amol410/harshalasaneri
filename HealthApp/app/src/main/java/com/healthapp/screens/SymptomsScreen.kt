package com.healthapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomsScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(1) }
    val items = listOf("Home", "Symptoms", "Records", "Profile")
    var symptomText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedBodyPart by remember { mutableStateOf<String?>(null) }
    var isFrontView by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Possible Conditions") },
            text = {
                Column {
                    Text("Based on your symptoms, here are the top 3 possible conditions:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. Common Cold (80%)", fontWeight = FontWeight.Bold)
                    Text("2. Seasonal Allergies (65%)", fontWeight = FontWeight.Bold)
                    Text("3. Influenza (40%)", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Please consult a doctor for an accurate diagnosis.", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Symptom Checker") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
                        1 -> navController.navigate("symptoms")
                        2 -> navController.navigate("records")
                        3 -> navController.navigate("profile")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Body Map Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Body Part", fontWeight = FontWeight.Bold)
                        TextButton(onClick = { isFrontView = !isFrontView }) {
                            Text(if (isFrontView) "Switch to Back" else "Switch to Front")
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simplified Body Map using Canvas
                        Canvas(modifier = Modifier.size(200.dp, 250.dp)) {
                            val bodyColor = Color.LightGray
                            val headCenter = Offset(size.width / 2, 30.dp.toPx())
                            val bodyTop = Offset(size.width / 2, 60.dp.toPx())
                            val bodyBottom = Offset(size.width / 2, 150.dp.toPx())
                            
                            // Head
                            drawCircle(color = bodyColor, radius = 25.dp.toPx(), center = headCenter)
                            
                            // Body
                            drawLine(color = bodyColor, start = bodyTop, end = bodyBottom, strokeWidth = 40.dp.toPx())
                            
                            // Arms
                            drawLine(color = bodyColor, start = Offset(size.width / 2 - 20.dp.toPx(), 70.dp.toPx()), end = Offset(size.width / 2 - 60.dp.toPx(), 120.dp.toPx()), strokeWidth = 15.dp.toPx())
                            drawLine(color = bodyColor, start = Offset(size.width / 2 + 20.dp.toPx(), 70.dp.toPx()), end = Offset(size.width / 2 + 60.dp.toPx(), 120.dp.toPx()), strokeWidth = 15.dp.toPx())
                            
                            // Legs
                            drawLine(color = bodyColor, start = Offset(size.width / 2 - 10.dp.toPx(), 150.dp.toPx()), end = Offset(size.width / 2 - 30.dp.toPx(), 230.dp.toPx()), strokeWidth = 15.dp.toPx())
                            drawLine(color = bodyColor, start = Offset(size.width / 2 + 10.dp.toPx(), 150.dp.toPx()), end = Offset(size.width / 2 + 30.dp.toPx(), 230.dp.toPx()), strokeWidth = 15.dp.toPx())
                        }
                        
                        // Interactive Points (Mock)
                        Box(modifier = Modifier.offset(y = (-80).dp).size(40.dp).clip(CircleShape).background(if (selectedBodyPart == "Head") Color.Red.copy(alpha = 0.5f) else Color.Transparent).clickable { selectedBodyPart = "Head" })
                        Box(modifier = Modifier.offset(y = (-20).dp).size(60.dp).clip(CircleShape).background(if (selectedBodyPart == "Chest") Color.Red.copy(alpha = 0.5f) else Color.Transparent).clickable { selectedBodyPart = "Chest" })
                    }
                    
                    if (selectedBodyPart != null) {
                        Text("Selected: $selectedBodyPart", modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            }

            OutlinedTextField(
                value = symptomText,
                onValueChange = { symptomText = it },
                label = { Text("Describe your symptoms") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Severity Level",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val severityOptions = listOf("Mild", "Moderate", "Severe")
                    var selectedSeverity by remember { mutableStateOf(severityOptions[0]) }
                    
                    severityOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (selectedSeverity == option),
                                    onClick = { selectedSeverity = option }
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedSeverity == option),
                                onClick = { selectedSeverity = option }
                            )
                            Text(
                                text = option,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Check Possible Conditions")
            }
        }
    }
}