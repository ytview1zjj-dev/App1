package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
  primary = BrandPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFEEF2FF),
  onPrimaryContainer = BrandPrimary,
  secondary = BrandSecondary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFECFEFF),
  onSecondaryContainer = Color(0xFF0E7490),
  tertiary = BrandPurple,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFFF5F3FF),
  onTertiaryContainer = Color(0xFF6D28D9),
  background = PureWhite,
  onBackground = Slate900,
  surface = PureWhite,
  onSurface = Slate900,
  surfaceVariant = Slate50,
  onSurfaceVariant = Slate700,
  outline = Slate200,
  outlineVariant = Slate100,
  error = BrandAlert,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = AppColorScheme,
    typography = Typography,
    content = content
  )
}
