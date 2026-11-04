package com.pactogg.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PactoColors = darkColorScheme(
    primary = PactoOrange,
    onPrimary = Color(0xFF17100A),
    secondary = PactoMuted,
    secondaryContainer = PactoSurface,
    onSecondaryContainer = PactoOrange,
    background = PactoBackground,
    onBackground = PactoText,
    surface = PactoSurface,
    onSurface = PactoText,
    onSurfaceVariant = PactoMuted,
    outline = PactoBorder,
)

@Composable
fun PactoGGTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PactoColors, typography = Typography, content = content)
}
