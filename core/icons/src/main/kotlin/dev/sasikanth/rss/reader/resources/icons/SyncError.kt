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

val RsserIcons.SyncError: ImageVector
  get() = rsserSyncError
private val rsserSyncError by lazy {
  rsserIcon(
    "SyncError",
    "M20 7V3M20 7H16M20 7C16 1 7 2 4 8M4 17V21M4 17H8M4 17C8 23 17 22 20 16M12 8V13M12 16V16.1",
  )
}
