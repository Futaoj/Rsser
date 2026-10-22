package dev.sasikanth.rss.reader.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

internal fun rsserIcon(name: String, data: String, filled: Boolean = false): ImageVector =
  ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f)
    .apply {
      addPath(
        pathData = PathParser().parsePathString(data).toNodes(),
        fill = if (filled) SolidColor(Color.Black) else null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.65f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
      )
    }
    .build()
