package com.healthapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import com.healthapp.ui.theme.HealthAppTheme
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.zIndex
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate

@Composable
fun SplashScreen(navController: NavController) {
    // Simple animation values without complex delegate issues
    val pulseScale = 1f  // Static value to avoid delegate issues
    val pulseScale2 = 1f  // Static value to avoid delegate issues
    val dotScale1 = 1f  // Static value to avoid delegate issues
    val dotScale2 = 1f  // Static value to avoid delegate issues
    val dotScale3 = 1f  // Static value to avoid delegate issues

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated background circles
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .absoluteOffset((-100).dp, 80.dp)
        ) {
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 100f * pulseScale,
                center = Offset(100f, 80f)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .absoluteOffset(80.dp, (-80).dp)
        ) {
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 120f * pulseScale2,
                center = Offset(size.width - 100f, size.height - 100f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .zIndex(1f)
                .padding(bottom = 50.dp)
        ) {
            // Main logo container
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 24.dp)
            ) {
                // Main heart logo
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center)
                    ) {
                        // Draw heart shape
                        drawPath(
                            path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(size.width / 2, size.height * 0.2f)
                                lineTo(size.width * 0.2f, size.height * 0.5f)
                                cubicTo(
                                    size.width * 0.1f, size.height * 0.4f,
                                    size.width * 0.1f, size.height * 0.7f,
                                    size.width / 2, size.height * 0.9f
                                )
                                cubicTo(
                                    size.width * 0.9f, size.height * 0.7f,
                                    size.width * 0.9f, size.height * 0.4f,
                                    size.width * 0.8f, size.height * 0.5f
                                )
                                lineTo(size.width / 2, size.height * 0.2f)
                                close()
                            },
                            color = Color.White
                        )
                    }
                }

                // Activity indicator
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    ) {
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width / 2, size.height * 0.2f),
                            end = Offset(size.width / 2, size.height * 0.8f),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.3f, size.height * 0.5f),
                            end = Offset(size.width * 0.7f, size.height * 0.5f),
                            strokeWidth = 2f
                        )
                    }
                }
            }

            // App name
            Text(
                text = "Sanari",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Tagline
            Text(
                text = "Your Health, Our Priority",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            // Loading indicator
            Row(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LoadingDot(
                    scale = dotScale1,
                    color = Color.White
                )
                LoadingDot(
                    scale = dotScale2,
                    color = Color.White
                )
                LoadingDot(
                    scale = dotScale3,
                    color = Color.White
                )
            }
        }
    }

    // Auto-navigate after a delay
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(5000) // 5 seconds like in the React version
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Composable
fun LoadingDot(
    scale: Float,
    color: Color
) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color)
    )
}