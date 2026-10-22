/*
 * Copyright 2026 Sasikanth Miriyampalli
 *
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.sasikanth.rss.reader.ui

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import dev.sasikanth.rss.reader.utils.LocalAmoledSetting

@Composable
internal fun AppTheme(
  useDarkTheme: Boolean = false,
  typography: Typography = typography(androidx.compose.ui.text.font.FontFamily.SansSerif),
  content: @Composable () -> Unit,
) {
  val useAmoled = LocalAmoledSetting.current
  val values = rsserColorValues(useDarkTheme)
  val colorScheme =
    remember(useDarkTheme, useAmoled) {
      AppColorScheme(values).apply { updateFrom(values, amoled = useDarkTheme && useAmoled) }
    }
  val materialColors =
    (if (useDarkTheme) darkColorScheme() else lightColorScheme()).copy(
      primary = colorScheme.primary,
      onPrimary = colorScheme.onPrimary,
      primaryContainer = colorScheme.primaryContainer,
      onPrimaryContainer = colorScheme.onPrimaryContainer,
      secondary = colorScheme.secondary,
      onSecondary = colorScheme.onPrimary,
      secondaryContainer = colorScheme.surfaceContainer,
      onSecondaryContainer = colorScheme.onSurface,
      tertiary = colorScheme.primary,
      onTertiary = colorScheme.onPrimary,
      tertiaryContainer = colorScheme.primaryContainer,
      onTertiaryContainer = colorScheme.onPrimaryContainer,
      background = colorScheme.backdrop,
      onBackground = colorScheme.onSurface,
      surface = colorScheme.surface,
      onSurface = colorScheme.onSurface,
      surfaceVariant = colorScheme.surfaceContainer,
      onSurfaceVariant = colorScheme.onSurfaceVariant,
      surfaceTint = colorScheme.primary,
      surfaceContainer = colorScheme.surfaceContainer,
      surfaceContainerLow = colorScheme.surfaceContainerLow,
      surfaceContainerLowest = colorScheme.surfaceContainerLowest,
      surfaceContainerHigh = colorScheme.surfaceContainerHigh,
      surfaceContainerHighest = colorScheme.surfaceContainerHighest,
      surfaceBright = colorScheme.surface,
      surfaceDim = colorScheme.surfaceContainerHigh,
      inverseSurface = colorScheme.inverseSurface,
      inverseOnSurface = colorScheme.inverseOnSurface,
      inversePrimary = colorScheme.inversePrimary,
      outline = colorScheme.outline,
      outlineVariant = colorScheme.outlineVariant,
      error = colorScheme.error,
    )
  val translucentStyles =
    remember(colorScheme) {
      TranslucentStyles(
        default =
          TranslucentStyle(
            background = colorScheme.surfaceContainerLow,
            outline = colorScheme.outlineVariant,
            foreground = colorScheme.onSurface,
          ),
        prominent =
          TranslucentStyle(
            background = colorScheme.surfaceContainerHigh,
            outline = colorScheme.outlineVariant,
            foreground = colorScheme.onSurface,
          ),
      )
    }
  @Suppress("DEPRECATION")
  val ripple =
    remember(colorScheme) {
      RippleConfiguration(color = colorScheme.primary, rippleAlpha = DefaultRippleAlpha)
    }
  CompositionLocalProvider(
    LocalIsDarkTheme provides useDarkTheme,
    LocalAppColorScheme provides colorScheme,
    LocalTranslucentStyles provides translucentStyles,
    LocalRippleConfiguration provides ripple,
  ) {
    MaterialTheme(
      colorScheme = materialColors,
      typography = typography,
      shapes =
        androidx.compose.material3.Shapes(
          extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
          small = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
          medium = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
          large = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
          extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        ),
      content = content,
    )
  }
}

internal object AppTheme {

  val colorScheme: AppColorScheme
    @Composable @ReadOnlyComposable get() = LocalAppColorScheme.current

  val isDark: Boolean
    @Composable @ReadOnlyComposable get() = LocalIsDarkTheme.current
}

internal val DefaultRippleAlpha =
  RippleAlpha(
    pressedAlpha = 0.16f,
    focusedAlpha = 0.24f,
    draggedAlpha = 0.24f,
    hoveredAlpha = 0.08f,
  )

internal val LocalIsDarkTheme = staticCompositionLocalOf { false }
