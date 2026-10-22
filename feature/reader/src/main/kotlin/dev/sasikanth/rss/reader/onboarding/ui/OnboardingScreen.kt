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

package dev.sasikanth.rss.reader.onboarding.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.rss.reader.onboarding.*
import dev.sasikanth.rss.reader.resources.icons.*

@Composable
internal fun OnboardingScreen(
  viewModel: OnboardingViewModel,
  onOnboardingDone: () -> Unit,
  onNavigateToDiscovery: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.effects.collect { effect ->
      when (effect) {
        OnboardingEffect.NavigateToHome -> onOnboardingDone()
        OnboardingEffect.NavigateToDiscovery -> onNavigateToDiscovery()
      }
    }
  }

  OnboardingContent(state = state, onEvent = viewModel::dispatch, modifier = modifier)
}

@Composable
private fun OnboardingContent(
  state: OnboardingState,
  onEvent: (OnboardingEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = MaterialTheme.colorScheme
  Scaffold(
    modifier = modifier,
    containerColor = colors.background,
    bottomBar = {
      Column(
        Modifier.fillMaxWidth().navigationBarsPadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
          "你的订阅与收藏，保存在自己的设备。",
          style = MaterialTheme.typography.bodySmall,
          color = colors.onSurfaceVariant,
        )
        Button(
          onClick = { onEvent(OnboardingEvent.GetStartedClicked) },
          enabled = !state.isPrePopulating,
          modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth().height(56.dp),
          shape = RoundedCornerShape(14.dp),
        ) {
          if (state.isPrePopulating)
            CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
          else Text("发现中文好内容", style = MaterialTheme.typography.titleMedium)
        }
      }
    },
  ) { padding ->
    Column(
      Modifier.fillMaxSize().padding(padding).padding(horizontal = 32.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Surface(shape = RoundedCornerShape(24.dp), color = colors.primaryContainer) {
        Icon(TwineIcons.RsserMark, null, Modifier.padding(24.dp).size(48.dp), tint = colors.primary)
      }
      Spacer(Modifier.height(28.dp))
      Text("Rsser", style = MaterialTheme.typography.displayLarge, fontFamily = FontFamily.Serif)
      Spacer(Modifier.height(24.dp))
      Text(
        "给阅读，留一点空间。",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(16.dp))
      Text(
        "关注你在意的声音。\n安静阅读，自由思考，与 AI 一起探索。",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = colors.onSurfaceVariant,
      )
    }
  }
}
