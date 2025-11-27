package com.healthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.R
import com.healthapp.ui.theme.HealthAppTheme
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.drawBehind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    val isLogin = remember { mutableStateOf(true) }
    val showPassword = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Section
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            androidx.compose.foundation.Canvas(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                            ) {
                                // Draw heart shape
                                drawPath(
                                    path = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(size.width / 2, size.height * 0.3f)
                                        lineTo(size.width * 0.3f, size.height * 0.6f)
                                        cubicTo(
                                            size.width * 0.2f, size.height * 0.5f,
                                            size.width * 0.2f, size.height * 0.8f,
                                            size.width / 2, size.height
                                        )
                                        cubicTo(
                                            size.width * 0.8f, size.height * 0.8f,
                                            size.width * 0.8f, size.height * 0.5f,
                                            size.width * 0.7f, size.height * 0.6f
                                        )
                                        lineTo(size.width / 2, size.height * 0.3f)
                                        close()
                                    },
                                    color = Color.White
                                )
                            }
                        }

                        // Activity indicator
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary)
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                        ) {
                            androidx.compose.foundation.Canvas(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.Center)
                            ) {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(size.width / 2, size.height * 0.2f),
                                    end = Offset(size.width / 2, size.height * 0.8f),
                                    strokeWidth = 1.5f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(size.width * 0.3f, size.height * 0.5f),
                                    end = Offset(size.width * 0.7f, size.height * 0.5f),
                                    strokeWidth = 1.5f
                                )
                            }
                        }
                    }

                    Text(
                        text = "Sanari",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Title with animation
                    AnimatedVisibility(visible = isLogin.value) {
                        Text(
                            text = "Welcome Back",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    AnimatedVisibility(visible = !isLogin.value) {
                        Text(
                            text = "Create Account",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Form
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Name field (only for signup)
                        AnimatedVisibility(visible = !isLogin.value) {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                label = { Text("Full Name") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        // Email field
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        // Phone field (only for signup)
                        AnimatedVisibility(visible = !isLogin.value) {
                            OutlinedTextField(
                                value = phone.value,
                                onValueChange = { phone.value = it },
                                label = { Text("Phone Number") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        // Password field
                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { showPassword.value = !showPassword.value }
                                ) {
                                    Icon(
                                        imageVector = if (showPassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Forgot password (only for login)
                        if (isLogin.value) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { /* Handle forgot password */ }
                                ) {
                                    Text(
                                        text = "Forgot Password?",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit button
                    Button(
                        onClick = {
                            if (isLogin.value) {
                                // Handle login
                                if (email.value.isEmpty() || password.value.isEmpty()) {
                                    // Show error message
                                } else {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } else {
                                // Handle signup
                                if (name.value.isEmpty() || email.value.isEmpty() || 
                                    phone.value.isEmpty() || password.value.isEmpty()) {
                                    // Show error message
                                } else if (password.value.length < 6) {
                                    // Show password error
                                } else {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = if (isLogin.value) "Login" else "Create Account",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Switch between login/signup
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isLogin.value) "Don't have an account?" else "Already have an account?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = {
                                isLogin.value = !isLogin.value
                                // Clear form when switching
                                name.value = ""
                                email.value = ""
                                phone.value = ""
                                password.value = ""
                                showPassword.value = false
                            }
                        ) {
                            Text(
                                text = if (isLogin.value) "Sign Up" else "Login",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Divider
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "Or continue with",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    // Social login buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* Handle Google login */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mail, // Using a placeholder icon since actual Google icon is not available
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Google",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = { /* Handle Facebook login */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face, // Using a placeholder icon since actual Facebook icon is not available
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Facebook",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}