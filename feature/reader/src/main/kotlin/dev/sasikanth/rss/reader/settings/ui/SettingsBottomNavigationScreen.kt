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

package dev.sasikanth.rss.reader.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.rss.reader.core.model.local.HomeNavigationItem
import dev.sasikanth.rss.reader.components.SimpleTopAppBar
import dev.sasikanth.rss.reader.main.ui.MainDestination
import dev.sasikanth.rss.reader.resources.icons.ArrowDown
import dev.sasikanth.rss.reader.resources.icons.ArrowUp
import dev.sasikanth.rss.reader.resources.icons.DropdownIcon
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import dev.sasikanth.rss.reader.settings.SettingsEvent
import dev.sasikanth.rss.reader.settings.SettingsViewModel
import dev.sasikanth.rss.reader.ui.AppTheme
import org.jetbrains.compose.resources.stringResource
import rsser.shared.generated.resources.Res
import rsser.shared.generated.resources.settingsBottomNavigationHint
import rsser.shared.generated.resources.settingsBottomNavigationMoveDown
import rsser.shared.generated.resources.settingsBottomNavigationMoveUp
import rsser.shared.generated.resources.settingsBottomNavigationSelect
import rsser.shared.generated.resources.settingsBottomNavigationSlot
import rsser.shared.generated.resources.settingsBottomNavigationTitle

@Composable
internal fun SettingsBottomNavigationScreen(
  viewModel: SettingsViewModel,
  goBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val layoutDirection = LocalLayoutDirection.current

  Scaffold(
    modifier = modifier,
    topBar = {
      SimpleTopAppBar(
        title = stringResource(Res.string.settingsBottomNavigationTitle),
        onBackClick = goBack,
      )
    },
    containerColor = AppTheme.colorScheme.backdrop,
    contentColor = AppTheme.colorScheme.onSurface,
  ) { padding ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding =
        PaddingValues(
          start = padding.calculateStartPadding(layoutDirection) + settingsItemHorizontalPadding,
          top = padding.calculateTopPadding() + 8.dp,
          end = padding.calculateEndPadding(layoutDirection) + settingsItemHorizontalPadding,
          bottom = padding.calculateBottomPadding() + 80.dp,
        ),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      item {
        Text(
          text = stringResource(Res.string.settingsBottomNavigationHint),
          style = MaterialTheme.typography.bodyMedium,
          color = AppTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
      }

      itemsIndexed(state.homeNavigationItems) { index, currentItem ->
        var expandedSlot by remember { mutableIntStateOf(-1) }
        val currentDestination = MainDestination.entries.first { it.navigationItem == currentItem }
        val availableItems =
          HomeNavigationItem.entries.filter { candidate ->
            candidate == currentItem ||
              state.homeNavigationItems.withIndex().none { (otherIndex, selected) ->
                otherIndex != index && selected == candidate
              }
          }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
          Text(
            text = stringResource(Res.string.settingsBottomNavigationSlot, index + 1),
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp),
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
              OutlinedButton(
                onClick = { expandedSlot = index },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
              ) {
                Icon(currentDestination.icon, contentDescription = null)
                Text(
                  text = stringResource(currentDestination.label),
                  modifier = Modifier.weight(1f).padding(start = 10.dp),
                )
                Icon(
                  imageVector = RsserIcons.DropdownIcon,
                  contentDescription = stringResource(Res.string.settingsBottomNavigationSelect),
                )
              }
              DropdownMenu(
                expanded = expandedSlot == index,
                onDismissRequest = { expandedSlot = -1 },
              ) {
                availableItems.forEach { item ->
                  val destination = MainDestination.entries.first { it.navigationItem == item }
                  DropdownMenuItem(
                    text = { Text(stringResource(destination.label)) },
                    leadingIcon = { Icon(destination.icon, contentDescription = null) },
                    onClick = {
                      val updated = state.homeNavigationItems.toMutableList()
                      updated[index] = item
                      viewModel.dispatch(SettingsEvent.UpdateHomeNavigationItems(updated))
                      expandedSlot = -1
                    },
                  )
                }
              }
            }

            IconButton(
              enabled = index > 0,
              onClick = {
                val updated = state.homeNavigationItems.toMutableList()
                val previous = updated[index - 1]
                updated[index - 1] = updated[index]
                updated[index] = previous
                viewModel.dispatch(SettingsEvent.UpdateHomeNavigationItems(updated))
              },
            ) {
              Icon(
                imageVector = RsserIcons.ArrowUp,
                contentDescription = stringResource(Res.string.settingsBottomNavigationMoveUp),
              )
            }
            IconButton(
              enabled = index < state.homeNavigationItems.lastIndex,
              onClick = {
                val updated = state.homeNavigationItems.toMutableList()
                val next = updated[index + 1]
                updated[index + 1] = updated[index]
                updated[index] = next
                viewModel.dispatch(SettingsEvent.UpdateHomeNavigationItems(updated))
              },
            ) {
              Icon(
                imageVector = RsserIcons.ArrowDown,
                contentDescription = stringResource(Res.string.settingsBottomNavigationMoveDown),
              )
            }
          }
        }
      }
    }
  }
}
