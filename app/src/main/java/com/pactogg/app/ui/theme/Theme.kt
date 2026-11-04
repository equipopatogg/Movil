package com.pactogg.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PactoColors = darkColorScheme(
    primary = PactoOrange,
    onPrimary = Color(0xFF17100A),
    secondary = PactoCyan,
    onSecondary = PactoBackground,
    tertiary = PactoLavender,
    onTertiary = PactoBackground,
    tertiaryContainer = PactoArtSurface,
    onTertiaryContainer = Color.White,
    secondaryContainer = PactoRaised,
    onSecondaryContainer = PactoCyan,
    background = PactoBackground,
    onBackground = PactoText,
    surface = PactoSurface,
    onSurface = PactoText,
    surfaceVariant = PactoRaised,
    surfaceContainer = PactoSurface,
    surfaceContainerLow = PactoInput,
    surfaceContainerHigh = PactoRaised,
    inverseSurface = PactoText,
    inverseOnSurface = PactoBackground,
    onSurfaceVariant = PactoMuted,
    outline = PactoBorder,
    outlineVariant = PactoBorder,
)

@Composable
fun PactoGGTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PactoColors, typography = Typography, content = content)
}
