package dev.sasikanth.rss.reader.ui

import androidx.compose.ui.graphics.Color

internal fun rsserColorValues(dark: Boolean): AppColorValues {
  fun tone(light: Long, night: Long) = Color(if (dark) night else light)
  return AppColorValues(
    primary = tone(0xFF57766A, 0xFFB2CDBF),
    onPrimary = tone(0xFFFFFFFF, 0xFF20372E),
    secondary = tone(0xFF57766A, 0xFFB2CDBF),
    outline = tone(0xFF858D87, 0xFF8C9690),
    outlineVariant = tone(0xFFE4E7E1, 0xFF414A44),
    primaryContainer = tone(0xFFE7EEE8, 0xFF34483D),
    onPrimaryContainer = tone(0xFF344F43, 0xFFD4E5DA),
    surface = tone(0xFFFAF9F6, 0xFF242B28),
    onSurface = tone(0xFF242B28, 0xFFE9ECE8),
    onSurfaceVariant = tone(0xFF717873, 0xFFB6BDB7),
    surfaceContainer = tone(0xFFF0F2EC, 0xFF2E3531),
    surfaceContainerLow = tone(0xFFF4F5F0, 0xFF29302C),
    surfaceContainerLowest = tone(0xFFFFFFFF, 0xFF1B211E),
    surfaceContainerHigh = tone(0xFFEAEDE7, 0xFF373E3A),
    surfaceContainerHighest = tone(0xFFE2E6DF, 0xFF414844),
    inversePrimary = tone(0xFFB2CDBF, 0xFF57766A),
    inverseSurface = tone(0xFF242B28, 0xFFE9ECE8),
    inverseOnSurface = tone(0xFFFAF9F6, 0xFF242B28),
    backdrop = tone(0xFFFAF9F6, 0xFF242B28),
    bottomSheet = tone(0xFFFAF9F6, 0xFF2E3531),
    bottomSheetInverse = tone(0xFF242B28, 0xFFFAF9F6),
    bottomSheetBorder = tone(0xFFE4E7E1, 0xFF414A44),
    error = tone(0xFFB33D32, 0xFFFFB4AB),
  )
}
