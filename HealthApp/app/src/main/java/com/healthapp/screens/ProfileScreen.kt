package com.healthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.navigation.NavController
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import com.healthapp.ui.theme.HealthAppTheme

data class ProfileData(
    val name: String,
    val age: String,
    val bloodGroup: String,
    val permanentIllness: String,
    val email: String,
    val phone: String,
    val emergencyContact: String,
    val address: String,
    val profileImage: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    var profileData by remember {
        mutableStateOf(
            ProfileData(
                name = "John Doe",
                age = "32",
                bloodGroup = "A+",
                permanentIllness = "None",
                email = "john.doe@example.com",
                phone = "+1 234 567 8900",
                emergencyContact = "+1 234 567 8901",
                address = "123 Health Street, Medical City",
                profileImage = null
            )
        )
    }
    
    var tempData by remember { mutableStateOf(profileData) }
    
    // Blood groups list
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    
    // Initialize tempData when entering edit mode
    LaunchedEffect(isEditing) {
        if (isEditing) {
            tempData = profileData
        }
    }
    
    // Handle save
    fun handleSave() {
        profileData = tempData
        isEditing = false
    }
    
    // Handle cancel
    fun handleCancel() {
        tempData = profileData
        isEditing = false
    }
    
    // Handle logout
    fun handleLogout() {
        showLogoutDialog = false
        onLogout()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ) {
                        Text("Premium")
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
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        if (profileData.profileImage != null) {
                            Icon(
                                Icons.Default.Person, // Placeholder since we don't have an actual image
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.BottomEnd)
                            .clickable { /* Handle image upload */ }
                            .padding(4.dp)
                    ) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = "Change Photo",
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                }
            }

            // User Name
            Text(
                text = if (isEditing) tempData.name else profileData.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            
            Text(
                text = profileData.email,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Quick Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Age Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isEditing) tempData.age else profileData.age,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Age",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Blood Group Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isEditing) tempData.bloodGroup else profileData.bloodGroup,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Blood",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Records Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "10",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Records",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Profile Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Personal Information Section
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Name
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.name,
                            onValueChange = { tempData = tempData.copy(name = it) },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.name,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Age
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.age,
                            onValueChange = { 
                                if (it.all { char -> char.isDigit() }) {
                                    tempData = tempData.copy(age = it)
                                }
                            },
                            label = { Text("Age") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.age,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Age") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Blood Group
                    if (isEditing) {
                        ExposedDropdownMenuBox(
                            expanded = false,
                            onExpandedChange = { /* Handle dropdown */ }
                        ) {
                            OutlinedTextField(
                                value = tempData.bloodGroup,
                                onValueChange = { tempData = tempData.copy(bloodGroup = it) },
                                label = { Text("Blood Group") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                },
                                readOnly = true
                            )
                            ExposedDropdownMenu(
                                expanded = false,
                                onDismissRequest = { /* Handle dismiss */ }
                            ) {
                                bloodGroups.forEach { group ->
                                    DropdownMenuItem(
                                        text = { Text(group) },
                                        onClick = { 
                                            tempData = tempData.copy(bloodGroup = group)
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = profileData.bloodGroup,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Blood Group") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Email
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.email,
                            onValueChange = { tempData = tempData.copy(email = it) },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.email,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Phone
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.phone,
                            onValueChange = { tempData = tempData.copy(phone = it) },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.phone,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Medical Information Section
                    Text(
                        text = "Medical Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Permanent Illness
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.permanentIllness,
                            onValueChange = { tempData = tempData.copy(permanentIllness = it) },
                            label = { Text("Permanent Illness / Chronic Conditions") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.permanentIllness,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Permanent Illness / Chronic Conditions") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            maxLines = 4
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Emergency Contact
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.emergencyContact,
                            onValueChange = { tempData = tempData.copy(emergencyContact = it) },
                            label = { Text("Emergency Contact") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.emergencyContact,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Emergency Contact") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Address
                    if (isEditing) {
                        OutlinedTextField(
                            value = tempData.address,
                            onValueChange = { tempData = tempData.copy(address = it) },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )
                    } else {
                        OutlinedTextField(
                            value = profileData.address,
                            onValueChange = { /* Read-only */ },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            maxLines = 4
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { handleCancel() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { handleSave() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Save Changes")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Edit Profile")
                    }
                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Log Out")
                    }
                }
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out? You'll need to sign in again to access your account.") },
            confirmButton = {
                Button(
                    onClick = { handleLogout() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}