package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val IndustrialColorScheme = darkColorScheme(
    primary = IndustrialOrange,
    onPrimary = IndustrialBlack,
    primaryContainer = IndustrialOrangeDark,
    onPrimaryContainer = IndustrialTextPrimary,
    secondary = IndustrialOrangeLight,
    onSecondary = IndustrialBlack,
    background = IndustrialBlack,
    onBackground = IndustrialTextPrimary,
    surface = IndustrialDarkCard,
    onSurface = IndustrialTextPrimary,
    surfaceVariant = IndustrialSurfaceElevated,
    onSurfaceVariant = IndustrialTextSecondary,
    outline = IndustrialBorder,
    outlineVariant = IndustrialBorderLight,
    error = IndustrialDanger,
    onError = IndustrialTextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IndustrialColorScheme,
        typography = Typography,
        content = content
    )
}
