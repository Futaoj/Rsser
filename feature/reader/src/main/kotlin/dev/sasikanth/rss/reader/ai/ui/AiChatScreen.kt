package dev.sasikanth.rss.reader.ai.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.rss.reader.ai.AiChatError
import dev.sasikanth.rss.reader.ai.AiChatMessage
import dev.sasikanth.rss.reader.ai.AiChatViewModel
import dev.sasikanth.rss.reader.resources.icons.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiChatScreen(
  viewModel: AiChatViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  var input by rememberSaveable { mutableStateOf("") }
  val listState = rememberLazyListState()
  val colors = MaterialTheme.colorScheme

  LaunchedEffect(state.messages.size, state.isSending) {
    if (state.messages.isNotEmpty()) {
      listState.animateScrollToItem(state.messages.lastIndex + if (state.isSending) 1 else 0)
    }
  }

  Scaffold(
    modifier = modifier,
    containerColor = colors.background,
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              "Rsser",
              style = MaterialTheme.typography.titleLarge,
              fontFamily = FontFamily.Serif,
            )
            Text(
              "阅读助手 · DeepSeek",
              style = MaterialTheme.typography.labelSmall,
              color = colors.onSurfaceVariant,
            )
          }
        },
        navigationIcon = { IconButton(onClick = onBack) { Icon(RsserIcons.ArrowBack, "返回") } },
        actions = {
          IconButton(onClick = viewModel::openApiKeyDialog) {
            Icon(RsserIcons.Settings, "设置 DeepSeek API Key")
          }
          IconButton(
            onClick = viewModel::clearConversation,
            enabled = !state.isSending && state.messages.isNotEmpty(),
          ) {
            Icon(RsserIcons.Edit, "开始新对话")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background),
      )
    },
    bottomBar = {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Surface(
          shape = RoundedCornerShape(22.dp),
          color = colors.surfaceContainerLowest,
          border = BorderStroke(1.dp, colors.outlineVariant),
          shadowElevation = 1.dp,
        ) {
          Column(Modifier.fillMaxWidth().padding(8.dp)) {
            OutlinedTextField(
              value = input,
              onValueChange = { input = it },
              modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
              placeholder = { Text("今天想了解什么？", color = colors.onSurfaceVariant) },
              enabled = !state.isSending,
              maxLines = 5,
              colors =
                OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                  unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                  disabledBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                ),
            )
            Row(
              Modifier.fillMaxWidth().padding(start = 12.dp, end = 4.dp, bottom = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                "最近 20 篇 · 标题与摘要",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurfaceVariant,
              )
              FilledIconButton(
                onClick = {
                  viewModel.send(input)
                  if (state.hasApiKey) input = ""
                },
                enabled = input.isNotBlank() && !state.isSending,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = colors.primary),
              ) {
                if (state.isSending)
                  CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Icon(RsserIcons.ArrowUp, "发送消息")
              }
            }
          }
        }
        Text(
          "发送时会将文章标题、摘要及对话交给 DeepSeek。回答仅供参考。",
          style = MaterialTheme.typography.labelSmall,
          color = colors.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 4.dp),
        )
      }
    },
  ) { padding ->
    Column(Modifier.fillMaxSize().padding(padding)) {
      if (!state.hasApiKey) {
        Surface(
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
          shape = RoundedCornerShape(12.dp),
          color = colors.surfaceContainer,
        ) {
          Row(
            Modifier.padding(start = 14.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              "连接 DeepSeek，开启阅读对话",
              Modifier.weight(1f),
              style = MaterialTheme.typography.bodySmall,
            )
            TextButton(onClick = viewModel::openApiKeyDialog) { Text("设置密钥") }
          }
        }
      }
      state.error?.let {
        Text(
          it.message(),
          Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
          color = colors.error,
          style = MaterialTheme.typography.bodySmall,
        )
      }
      LazyColumn(
        state = listState,
        modifier = Modifier.weight(1f).fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
      ) {
        if (state.messages.isEmpty()) {
          item {
            Column(
              Modifier.fillMaxWidth().padding(top = 24.dp),
              verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
              Icon(RsserIcons.RsserMark, null, Modifier.size(38.dp), tint = colors.primary)
              Text("从阅读，开始对话。", style = MaterialTheme.typography.headlineMedium)
              Text(
                "让想法在订阅中生长。\n一起梳理重点，发现新的联系。",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
              )
              Spacer(Modifier.height(8.dp))
              listOf("帮我总结最近订阅的文章", "这些文章有哪些共同话题？", "推荐一篇值得深入阅读的文章").forEach { prompt ->
                OutlinedButton(
                  onClick = { input = prompt },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp),
                  border = BorderStroke(1.dp, colors.outlineVariant),
                  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                ) {
                  Text(
                    prompt,
                    Modifier.weight(1f),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                  )
                  Icon(RsserIcons.Add, null, Modifier.size(16.dp), tint = colors.onSurfaceVariant)
                }
              }
            }
          }
        }
        itemsIndexed(state.messages) { _, message -> ChatBubble(message) }
        if (state.isSending) {
          item {
            Row(
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
              Text(
                "正在阅读与思考…",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
              )
            }
          }
        }
      }
    }
  }
  if (state.showApiKeyDialog) {
    ApiKeyDialog(
      state.hasApiKey,
      viewModel::saveApiKey,
      viewModel::removeApiKey,
      viewModel::closeApiKeyDialog,
    )
  }
}

@Composable
private fun ChatBubble(message: AiChatMessage) {
  val user = message.role == "user"
  val colors = MaterialTheme.colorScheme
  Column(
    Modifier.fillMaxWidth(),
    horizontalAlignment = if (user) Alignment.End else Alignment.Start,
  ) {
    if (!user) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(RsserIcons.RsserMark, null, Modifier.size(20.dp), tint = colors.primary)
        Text("Rsser", style = MaterialTheme.typography.labelMedium, color = colors.onSurfaceVariant)
      }
      Spacer(Modifier.height(12.dp))
    }
    SelectionContainer {
      Text(
        message.content,
        modifier =
          if (user)
            Modifier.widthIn(max = 520.dp)
              .background(colors.surfaceContainer, RoundedCornerShape(18.dp))
              .padding(16.dp)
          else Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge,
        color = colors.onSurface,
      )
    }
  }
}

@Composable
private fun ApiKeyDialog(
  hasApiKey: Boolean,
  onSave: (String) -> Unit,
  onRemove: () -> Unit,
  onDismiss: () -> Unit,
) {
  var value by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("DeepSeek API Key") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Key 仅加密保存在本机，用于直接连接 DeepSeek。")
        OutlinedTextField(
          value = value,
          onValueChange = { value = it },
          modifier = Modifier.fillMaxWidth(),
          label = { Text(if (hasApiKey) "输入新 Key 以替换" else "API Key") },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          singleLine = true,
        )
      }
    },
    confirmButton = {
      TextButton(onClick = { onSave(value) }, enabled = value.isNotBlank()) { Text("保存") }
    },
    dismissButton = {
      Row {
        if (hasApiKey) TextButton(onClick = onRemove) { Text("删除 Key") }
        TextButton(onClick = onDismiss) { Text("取消") }
      }
    },
  )
}

@Composable
private fun AiChatError.message(): String =
  when (this) {
    AiChatError.MissingApiKey -> "请先设置 DeepSeek API Key。"
    AiChatError.InvalidApiKey -> "API Key 无效，请检查后重新设置。"
    AiChatError.RateLimited -> "请求过于频繁或额度不足，请稍后重试。"
    AiChatError.EmptyResponse -> "DeepSeek 没有返回内容，请重试。"
    AiChatError.Network -> "连接 DeepSeek 失败，请检查网络或稍后重试。"
  }
