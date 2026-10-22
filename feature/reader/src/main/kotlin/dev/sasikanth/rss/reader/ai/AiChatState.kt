package dev.sasikanth.rss.reader.ai

import androidx.compose.runtime.Immutable

@Immutable
data class AiChatMessage(val role: String, val content: String)

@Immutable
data class AiChatState(
  val messages: List<AiChatMessage> = emptyList(),
  val isSending: Boolean = false,
  val hasApiKey: Boolean = false,
  val showApiKeyDialog: Boolean = false,
  val error: AiChatError? = null,
)

enum class AiChatError {
  MissingApiKey,
  InvalidApiKey,
  RateLimited,
  EmptyResponse,
  Network,
}
