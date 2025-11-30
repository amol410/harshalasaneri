package com.healthapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JaguarGold,
    onPrimary = JaguarBlack,
    primaryContainer = JaguarCharcoal,
    onPrimaryContainer = JaguarGold,
    secondary = JaguarAccent,
    onSecondary = JaguarBlack,
    secondaryContainer = JaguarDarkGray,
    onSecondaryContainer = JaguarAccent,
    tertiary = JaguarGoldDim,
    onTertiary = JaguarBlack,
    tertiaryContainer = JaguarCharcoal,
    onTertiaryContainer = JaguarGoldDim,
    background = JaguarBlack,
    onBackground = JaguarTextPrimary,
    surface = JaguarCharcoal,
    onSurface = JaguarTextPrimary,
    surfaceVariant = JaguarDarkGray,
    onSurfaceVariant = JaguarTextSecondary,
    error = JaguarError,
    onError = JaguarBlack
)

private val LightColorScheme = lightColorScheme(
    primary = JaguarGold,
    onPrimary = JaguarBlack,
    primaryContainer = Color(0xFFF5F5F5),
    onPrimaryContainer = JaguarGoldDim,
    secondary = JaguarAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = JaguarAccent,
    tertiary = JaguarGoldDim,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF8E1),
    onTertiaryContainer = JaguarGoldDim,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF121212),
    surface = Color.White,
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF616161),
    error = JaguarError,
    onError = Color.White
)

@Composable
fun HealthAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}