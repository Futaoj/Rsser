package dev.sasikanth.rss.reader.ai.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownPadding
import dev.sasikanth.rss.reader.ai.AiChatError
import dev.sasikanth.rss.reader.ai.AiArticleCitation
import dev.sasikanth.rss.reader.ai.AiChatMessage
import dev.sasikanth.rss.reader.ai.AiChatViewModel
import dev.sasikanth.rss.reader.components.image.AsyncImage
import dev.sasikanth.rss.reader.platform.LocalLinkHandler
import dev.sasikanth.rss.reader.resources.icons.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiChatScreen(
  viewModel: AiChatViewModel,
  onBack: () -> Unit,
  onArticleClick: (AiArticleCitation) -> Unit,
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
              "阅读助手 · ${state.modelName}",
              style = MaterialTheme.typography.labelSmall,
              color = colors.onSurfaceVariant,
            )
          }
        },
        navigationIcon = { IconButton(onClick = onBack) { Icon(RsserIcons.ArrowBack, "返回") } },
        actions = {
          IconButton(onClick = viewModel::openApiKeyDialog) {
            Icon(RsserIcons.Settings, "设置 AI 服务")
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
          "发送时会将文章标题、摘要及对话交给所选 AI 服务。回答仅供参考。",
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
            TextButton(onClick = viewModel::openApiKeyDialog) { Text("去设置") }
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
        itemsIndexed(state.messages) { _, message -> ChatBubble(message, onArticleClick) }
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
      state.baseUrl,
      state.modelName,
      state.availableModels,
      state.modelsBaseUrl,
      state.isLoadingModels,
      state.modelLoadError,
      viewModel::refreshModels,
      viewModel::saveConfiguration,
      viewModel::removeApiKey,
      viewModel::closeApiKeyDialog,
    )
  }
}

@Composable
private fun ChatBubble(message: AiChatMessage, onArticleClick: (AiArticleCitation) -> Unit) {
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
    if (user) {
      SelectionContainer {
        Text(
          message.content,
          modifier =
            Modifier.widthIn(max = 520.dp)
              .background(colors.surfaceContainer, RoundedCornerShape(18.dp))
              .padding(16.dp),
          style = MaterialTheme.typography.bodyLarge,
          color = colors.onSurface,
        )
      }
    } else {
      AiAssistantMarkdown(message.content)
      if (message.citations.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        Text(
          "引用文章",
          style = MaterialTheme.typography.labelLarge,
          color = colors.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          message.citations.forEach { citation ->
            AiArticleCitationCard(citation, onClick = { onArticleClick(citation) })
          }
        }
      }
    }
  }
}

@Composable
private fun AiArticleCitationCard(citation: AiArticleCitation, onClick: () -> Unit) {
  val colors = MaterialTheme.colorScheme
  Surface(
    modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    color = colors.surfaceContainer,
    border = BorderStroke(1.dp, colors.outlineVariant),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(10.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val imageUrl = citation.imageUrl?.takeIf { it.startsWith("https://") || it.startsWith("http://") }
      if (imageUrl != null) {
        AsyncImage(
          url = imageUrl,
          contentDescription = null,
          modifier = Modifier.size(width = 92.dp, height = 72.dp).clip(RoundedCornerShape(10.dp)),
          contentScale = ContentScale.Crop,
        )
      } else {
        Box(
          modifier =
            Modifier.size(width = 92.dp, height = 72.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(colors.primaryContainer),
          contentAlignment = Alignment.Center,
        ) {
          Icon(RsserIcons.RsserMark, null, tint = colors.primary)
        }
      }
      Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          citation.title,
          style = MaterialTheme.typography.bodyMedium,
          color = colors.onSurface,
          maxLines = 2,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        )
        Text(
          citation.feedName,
          style = MaterialTheme.typography.labelSmall,
          color = colors.onSurfaceVariant,
          maxLines = 1,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
private fun AiAssistantMarkdown(content: String) {
  val colors = MaterialTheme.colorScheme
  val markdownColors =
    markdownColor(
      text = colors.onSurface,
      codeBackground = colors.surfaceContainer,
      dividerColor = colors.outlineVariant,
      tableBackground = colors.surfaceContainer.copy(alpha = 0.45f),
    )
  val markdownTypography =
    markdownTypography(
      h1 = MaterialTheme.typography.headlineSmall,
      h2 = MaterialTheme.typography.titleLarge,
      h3 = MaterialTheme.typography.titleMedium,
      h4 = MaterialTheme.typography.titleSmall,
      text = MaterialTheme.typography.bodyLarge,
      textLink =
        TextLinkStyles(
          MaterialTheme.typography.bodyLarge
            .copy(
              color = colors.primary,
              fontWeight = FontWeight.Medium,
              textDecoration = TextDecoration.Underline,
            )
            .toSpanStyle()
        ),
    )
  val linkHandler = LocalLinkHandler.current
  val coroutineScope = rememberCoroutineScope()
  val uriHandler =
    remember(linkHandler, coroutineScope) {
      object : UriHandler {
        override fun openUri(uri: String) {
          coroutineScope.launch { linkHandler.openLink(uri) }
        }
      }
    }

  SelectionContainer {
    CompositionLocalProvider(
      LocalUriHandler provides uriHandler,
      LocalMarkdownColors provides markdownColors,
      LocalMarkdownTypography provides markdownTypography,
      LocalMarkdownPadding provides markdownPadding(block = 8.dp),
    ) {
      Markdown(
        content = content,
        colors = markdownColors,
        typography = markdownTypography,
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ApiKeyDialog(
  hasApiKey: Boolean,
  baseUrl: String,
  modelName: String,
  availableModels: List<String>,
  modelsBaseUrl: String?,
  isLoadingModels: Boolean,
  modelLoadError: AiChatError?,
  onRefreshModels: (String, String) -> Unit,
  onSave: (String, String, String) -> Unit,
  onRemove: () -> Unit,
  onDismiss: () -> Unit,
) {
  var baseUrlValue by rememberSaveable(baseUrl) { mutableStateOf(baseUrl) }
  var apiKeyValue by rememberSaveable { mutableStateOf("") }
  var selectedModel by rememberSaveable(modelName) { mutableStateOf(modelName) }
  var modelMenuExpanded by remember { mutableStateOf(false) }
  val modelsForBaseUrl =
    availableModels.takeIf { modelsBaseUrl == baseUrlValue.trim().trimEnd('/') }.orEmpty()
  LaunchedEffect(modelsForBaseUrl) {
    if (modelsForBaseUrl.isNotEmpty() && selectedModel !in modelsForBaseUrl) {
      selectedModel = modelsForBaseUrl.first()
    }
  }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("AI 服务设置") },
    text = {
      Column(
        modifier = Modifier.heightIn(max = 480.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(
          "兼容 OpenAI Chat Completions 格式的服务。DeepSeek 默认地址：https://api.deepseek.com。模型列表从服务端自动获取。"
        )
        OutlinedTextField(
          value = baseUrlValue,
          onValueChange = { baseUrlValue = it },
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Base URL") },
          placeholder = { Text("https://api.deepseek.com") },
          supportingText = { Text("填写服务根地址，程序会自动追加 /chat/completions") },
          singleLine = true,
        )
        OutlinedTextField(
          value = apiKeyValue,
          onValueChange = { apiKeyValue = it },
          modifier = Modifier.fillMaxWidth(),
          label = { Text("API Key") },
          placeholder = { Text(if (hasApiKey) "已保存，留空则保持不变" else "输入 API Key") },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          singleLine = true,
        )
        ExposedDropdownMenuBox(
          expanded = modelMenuExpanded,
          onExpandedChange = { modelMenuExpanded = it && modelsForBaseUrl.isNotEmpty() },
        ) {
          OutlinedTextField(
            value = selectedModel.takeIf(modelsForBaseUrl::contains).orEmpty(),
            onValueChange = {},
            modifier =
              Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            label = { Text("Model") },
            placeholder = { Text("先获取可用模型") },
            readOnly = true,
            enabled = modelsForBaseUrl.isNotEmpty(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelMenuExpanded) },
            singleLine = true,
          )
          ExposedDropdownMenu(
            expanded = modelMenuExpanded,
            onDismissRequest = { modelMenuExpanded = false },
          ) {
            modelsForBaseUrl.forEach { model ->
              DropdownMenuItem(
                text = { Text(model) },
                onClick = {
                  selectedModel = model
                  modelMenuExpanded = false
                },
              )
            }
          }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          TextButton(
            onClick = { onRefreshModels(baseUrlValue, apiKeyValue) },
            enabled = !isLoadingModels && (hasApiKey || apiKeyValue.isNotBlank()),
          ) {
            if (isLoadingModels) {
              CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
              Spacer(Modifier.width(8.dp))
              Text("正在获取模型")
            } else {
              Text("刷新模型列表")
            }
          }
          if (modelsForBaseUrl.isNotEmpty()) {
            Text("共 ${modelsForBaseUrl.size} 个", style = MaterialTheme.typography.labelSmall)
          }
        }
        modelLoadError?.let { error ->
          Text(
            text = error.message(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
          )
        }
        Text(
          "API Key 会加密保存在本机。",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = { onSave(baseUrlValue, apiKeyValue, selectedModel) },
        enabled =
          baseUrlValue.isNotBlank() &&
            modelsForBaseUrl.contains(selectedModel) &&
            (hasApiKey || apiKeyValue.isNotBlank()),
      ) { Text("保存") }
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
    AiChatError.MissingApiKey -> "请先设置 API Key。"
    AiChatError.InvalidApiKey -> "API Key 无效，请检查后重新设置。"
    AiChatError.RateLimited -> "请求过于频繁，请稍后重试。"
    AiChatError.InsufficientCredits -> "账户余额或可用额度不足，请检查账户用量。"
    AiChatError.InvalidRequest -> "请求参数或模型配置不正确，请检查后重试。"
    AiChatError.ServiceUnavailable -> "AI 服务暂时不可用，请稍后重试。"
    AiChatError.ServiceError -> "AI 服务请求失败，请稍后重试。"
    AiChatError.EmptyResponse -> "AI 服务没有返回可用模型或内容，请重试。"
    AiChatError.Network -> "连接 AI 服务失败，请检查网络或稍后重试。"
    AiChatError.SecureConnection -> "与服务建立 HTTPS 安全连接失败。请检查模拟器网络、代理或 Base URL。"
  }
