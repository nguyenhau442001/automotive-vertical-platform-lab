package com.automotivevertical.drivecore.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CockpitBackground = Color(0xFF121418)
val CockpitCardBackground = Color(0xFF1E222A)
val CockpitCardBorder = Color(0xFF2C323E)
val CockpitAccent = Color(0xFF00E5FF)
val CockpitTextPrimary = Color(0xFFFFFFFF)
val CockpitTextSecondary = Color(0xFF8E99A8)
val CockpitSuccess = Color(0xFF00E676)
val CockpitWarning = Color(0xFFFFD600)
val CockpitDanger = Color(0xFFFF5252)
val CockpitGearActive = Color(0xFF00E5FF)
val CockpitGearInactive = Color(0xFF424B5A)

private val DarkColorScheme = darkColorScheme(
    primary = CockpitAccent,
    background = CockpitBackground,
    surface = CockpitCardBackground,
    onBackground = CockpitTextPrimary,
    onSurface = CockpitTextPrimary
)

@Composable
fun DriveCoreTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
