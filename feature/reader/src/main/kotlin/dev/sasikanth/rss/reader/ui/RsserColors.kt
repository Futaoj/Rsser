package dev.sasikanth.rss.reader.ui

import androidx.compose.ui.graphics.Color

internal fun rsserColorValues(dark: Boolean): AppColorValues {
  fun tone(light: Long, night: Long) = Color(if (dark) night else light)
  return AppColorValues(
    primary = tone(0xFFA84F35, 0xFFE8A58B),
    onPrimary = tone(0xFFFFFFFF, 0xFF37251F),
    secondary = tone(0xFF9B4D35, 0xFFE8A58B),
    outline = tone(0xFF8B8379, 0xFF9B9287),
    outlineVariant = tone(0xFFE4DFD6, 0xFF48433C),
    primaryContainer = tone(0xFFF0E1D8, 0xFF4C342B),
    onPrimaryContainer = tone(0xFF703822, 0xFFF5D5C7),
    surface = tone(0xFFFAF9F5, 0xFF262522),
    onSurface = tone(0xFF302D28, 0xFFF0EDE5),
    onSurfaceVariant = tone(0xFF706A61, 0xFFBBB4AA),
    surfaceContainer = tone(0xFFF1EFE8, 0xFF302E29),
    surfaceContainerLow = tone(0xFFF5F3ED, 0xFF2B2925),
    surfaceContainerLowest = tone(0xFFFFFFFF, 0xFF201F1C),
    surfaceContainerHigh = tone(0xFFEAE7DF, 0xFF39362F),
    surfaceContainerHighest = tone(0xFFE3DFD5, 0xFF434037),
    inversePrimary = tone(0xFFE8A58B, 0xFFA84F35),
    inverseSurface = tone(0xFF302D28, 0xFFF0EDE5),
    inverseOnSurface = tone(0xFFFAF9F5, 0xFF302D28),
    backdrop = tone(0xFFFAF9F5, 0xFF262522),
    bottomSheet = tone(0xFFFAF9F5, 0xFF302E29),
    bottomSheetInverse = tone(0xFF302D28, 0xFFFAF9F5),
    bottomSheetBorder = tone(0xFFE4DFD6, 0xFF48433C),
    error = tone(0xFFB33D32, 0xFFFFB4AB),
  )
}
