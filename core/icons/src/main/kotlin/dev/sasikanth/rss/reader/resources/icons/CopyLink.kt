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

package dev.sasikanth.rss.reader.resources.icons

import androidx.compose.ui.graphics.vector.ImageVector

val RsserIcons.CopyLink: ImageVector
  get() = rsserCopyLink

private val rsserCopyLink by lazy {
  rsserIcon(
    "CopyLink",
    "M10 14L14 10M8 16L6 18A4 4 0 0 1 1 13L6 8A4 4 0 0 1 12 8M16 8L18 6A4 4 0 0 1 23 11L18 16A4 4 0 0 1 12 16",
  )
}
