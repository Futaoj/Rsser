package dev.sasikanth.rss.reader.ai.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sasikanth.rss.reader.resources.icons.AiChat
import dev.sasikanth.rss.reader.resources.icons.ArrowUp
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import dev.sasikanth.rss.reader.utils.BOTTOM_BAR_MAX_WIDTH

@Composable
internal fun AiEntryBottomBar(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  nowPlayingChip: (@Composable RowScope.() -> Unit)? = null,
) {
  val colors = MaterialTheme.colorScheme
  Surface(
    modifier =
      modifier
        .widthIn(max = BOTTOM_BAR_MAX_WIDTH)
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = 12.dp),
    shape = RoundedCornerShape(20.dp),
    color = colors.surfaceContainerLowest,
    border = BorderStroke(1.dp, colors.outlineVariant),
    shadowElevation = 2.dp,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Row(
        Modifier.weight(1f)
          .clickable(onClick = onClick)
          .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Icon(RsserIcons.AiChat, null, tint = colors.primary)
        Column(Modifier.weight(1f)) {
          Text("和 Rsser 聊聊", style = MaterialTheme.typography.titleSmall)
          Text(
            "从你的订阅中，发现更多",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant,
          )
        }
        Icon(RsserIcons.ArrowUp, null, Modifier.size(20.dp), tint = colors.onSurfaceVariant)
      }
      nowPlayingChip?.invoke(this)
    }
  }
}
