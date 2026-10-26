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

package dev.sasikanth.rss.reader.reader.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sasikanth.rss.reader.resources.icons.ArticleShortcut
import dev.sasikanth.rss.reader.resources.icons.OpenBrowser
import dev.sasikanth.rss.reader.resources.icons.Pause
import dev.sasikanth.rss.reader.resources.icons.Play
import dev.sasikanth.rss.reader.resources.icons.Settings
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import dev.sasikanth.rss.reader.resources.icons.Bookmark
import dev.sasikanth.rss.reader.resources.icons.BookmarkFilled
import dev.sasikanth.rss.reader.resources.icons.CalendarClock
import dev.sasikanth.rss.reader.resources.icons.Share
import dev.sasikanth.rss.reader.resources.icons.CustomTypography
import dev.sasikanth.rss.reader.ui.AppColorScheme
import dev.sasikanth.rss.reader.ui.AppTheme
import org.jetbrains.compose.resources.stringResource
import rsser.shared.generated.resources.Res
import rsser.shared.generated.resources.cdLoadFullArticle
import rsser.shared.generated.resources.cdWatchVideo
import rsser.shared.generated.resources.openWebsite
import rsser.shared.generated.resources.pause
import rsser.shared.generated.resources.play
import rsser.shared.generated.resources.readerSettings
import rsser.shared.generated.resources.readerSave
import rsser.shared.generated.resources.readerReadLater
import rsser.shared.generated.resources.readerShare
import rsser.shared.generated.resources.readerCustomisationsTypeface

@Composable
internal fun ReaderViewBottomBar(
  selectedAppColorScheme: AppColorScheme?,
  loadFullArticle: Boolean,
  openInBrowserClick: () -> Unit,
  loadFullArticleClick: () -> Unit,
  openReaderViewSettings: () -> Unit,
  isBookmarked: Boolean,
  bookmarkClick: () -> Unit,
  readLaterClick: () -> Unit,
  shareClick: () -> Unit,
) {
  val colorScheme = selectedAppColorScheme ?: AppTheme.colorScheme
  Row(
    modifier = Modifier.fillMaxWidth().height(68.dp).padding(horizontal = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(0.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    ReaderAction(
      modifier = Modifier.weight(1f),
      label = stringResource(Res.string.readerCustomisationsTypeface),
      icon = RsserIcons.CustomTypography,
      onClick = openReaderViewSettings,
      color = colorScheme.onSurfaceVariant,
    )
    ReaderAction(
      modifier = Modifier.weight(1f),
      label = stringResource(Res.string.readerSave),
      icon = if (isBookmarked) RsserIcons.BookmarkFilled else RsserIcons.Bookmark,
      onClick = bookmarkClick,
      color = if (isBookmarked) colorScheme.primary else colorScheme.onSurfaceVariant,
    )
    ReaderAction(
      modifier = Modifier.weight(1f),
      label = stringResource(Res.string.readerReadLater),
      icon = RsserIcons.CalendarClock,
      onClick = readLaterClick,
      color = colorScheme.primary,
    )
    ReaderAction(
      modifier = Modifier.weight(1f),
      label = stringResource(Res.string.readerShare),
      icon = RsserIcons.Share,
      onClick = shareClick,
      color = colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun ReaderAction(
  modifier: Modifier = Modifier,
  label: String,
  icon: ImageVector,
  onClick: () -> Unit,
  color: Color,
) {
  Column(
    modifier = modifier.fillMaxHeight()
      .clip(RoundedCornerShape(8.dp)).clickable { onClick() }
      .semantics { role = Role.Button; contentDescription = label },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.requiredSize(22.dp))
    Text(
      label,
      color = color,
      style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
      maxLines = 1,
      softWrap = false,
    )
  }
}

@Composable
internal fun ReaderVideoBottomBar(
  selectedAppColorScheme: AppColorScheme?,
  openInBrowserClick: () -> Unit,
  watchVideoClick: () -> Unit,
  openReaderViewSettings: () -> Unit,
) {
  Row(
    modifier = Modifier.wrapContentWidth().height(IntrinsicSize.Min).padding(horizontal = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val colorScheme = selectedAppColorScheme ?: AppTheme.colorScheme

    BottomBarIconButton(
      modifier = Modifier.padding(vertical = 12.dp),
      label = stringResource(Res.string.openWebsite),
      icon = RsserIcons.OpenBrowser,
      onClick = openInBrowserClick,
      minWidth = 52.dp,
    )

    BottomBarToggleIconButton(
      modifier = Modifier.fillMaxHeight().padding(vertical = 8.dp),
      label = stringResource(Res.string.cdWatchVideo),
      icon = RsserIcons.Play,
      onClick = watchVideoClick,
      backgroundColor = colorScheme.primaryContainer,
      contentColor = colorScheme.onPrimaryContainer,
      minWidth = 88.dp,
    )

    BottomBarIconButton(
      modifier = Modifier.padding(vertical = 12.dp),
      label = stringResource(Res.string.readerSettings),
      icon = RsserIcons.Settings,
      onClick = openReaderViewSettings,
      minWidth = 52.dp,
    )
  }
}

@Composable
internal fun ReaderAudioBottomBar(
  selectedAppColorScheme: AppColorScheme?,
  isPlaying: Boolean,
  isBuffering: Boolean,
  openInBrowserClick: () -> Unit,
  playPauseClick: () -> Unit,
  openReaderViewSettings: () -> Unit,
) {
  Row(
    modifier = Modifier.wrapContentWidth().height(IntrinsicSize.Min).padding(horizontal = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val colorScheme = selectedAppColorScheme ?: AppTheme.colorScheme
    val transition = updateTransition(isPlaying)
    val playPauseWidth by
      transition.animateDp(
        transitionSpec = {
          spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy)
        }
      ) {
        if (it) {
          88.dp
        } else {
          72.dp
        }
      }
    val playPauseVerticalPadding by
      transition.animateDp(
        transitionSpec = {
          spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy)
        }
      ) {
        if (it) {
          8.dp
        } else {
          12.dp
        }
      }
    val playPauseBackgroundColor by
      transition.animateColor {
        if (it) {
          colorScheme.primaryContainer
        } else {
          colorScheme.surfaceContainerHighest
        }
      }
    val playPauseContentColor by
      transition.animateColor {
        if (it) {
          colorScheme.onPrimaryContainer
        } else {
          colorScheme.onSurface
        }
      }

    BottomBarIconButton(
      modifier = Modifier.padding(vertical = 12.dp),
      label = stringResource(Res.string.openWebsite),
      icon = RsserIcons.OpenBrowser,
      onClick = openInBrowserClick,
      minWidth = 52.dp,
    )

    BottomBarToggleIconButton(
      modifier = Modifier.fillMaxHeight().padding(vertical = playPauseVerticalPadding),
      label = stringResource(if (isPlaying) Res.string.pause else Res.string.play),
      icon = if (isPlaying) RsserIcons.Pause else RsserIcons.Play,
      onClick = playPauseClick,
      backgroundColor = playPauseBackgroundColor,
      contentColor = playPauseContentColor,
      minWidth = playPauseWidth,
      showProgress = isBuffering,
    )

    BottomBarIconButton(
      modifier = Modifier.padding(vertical = 12.dp),
      label = stringResource(Res.string.readerSettings),
      icon = RsserIcons.Settings,
      onClick = openReaderViewSettings,
      minWidth = 52.dp,
    )
  }
}

@Composable
private fun BottomBarIconButton(
  label: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  contentColor: Color = AppTheme.colorScheme.onSurfaceVariant,
  minWidth: Dp = 56.dp,
) {
  Box(
    modifier =
      Modifier.then(modifier)
        .requiredSizeIn(minWidth = minWidth)
        .clip(RoundedCornerShape(50))
        .semantics {
          role = Role.Button
          contentDescription = label
        }
        .clickable { onClick() },
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      modifier = Modifier.padding(vertical = 10.dp).requiredSize(20.dp),
      imageVector = icon,
      contentDescription = null,
      tint = contentColor,
    )
  }
}

@Composable
private fun BottomBarToggleIconButton(
  label: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.Transparent,
  contentColor: Color = AppTheme.colorScheme.onSurfaceVariant,
  minWidth: Dp = 72.dp,
  showProgress: Boolean = false,
) {
  Box(
    modifier =
      Modifier.requiredSizeIn(minHeight = 40.dp, minWidth = minWidth)
        .then(modifier)
        .clip(RoundedCornerShape(50))
        .background(backgroundColor, RoundedCornerShape(50))
        .semantics {
          role = Role.Button
          contentDescription = label
        }
        .clickable { onClick() },
    contentAlignment = Alignment.Center,
  ) {
    if (showProgress) {
      CircularProgressIndicator(
        modifier = Modifier.requiredSize(20.dp),
        color = contentColor,
        strokeWidth = 2.dp,
      )
    } else {
      Icon(
        modifier = Modifier.requiredSize(20.dp),
        imageVector = icon,
        contentDescription = null,
        tint = contentColor,
      )
    }
  }
}
