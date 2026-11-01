package dev.sasikanth.rss.reader.ai

import androidx.compose.runtime.Immutable

@Immutable
data class AiChatMessage(
  val role: String,
  val content: String,
  val citations: List<AiArticleCitation> = emptyList(),
)

@Immutable
data class AiArticleCitation(
  val postId: String,
  val title: String,
  val feedName: String,
  val imageUrl: String?,
)

@Immutable
data class AiChatState(
  val messages: List<AiChatMessage> = emptyList(),
  val isSending: Boolean = false,
  val hasApiKey: Boolean = false,
  val showApiKeyDialog: Boolean = false,
  val baseUrl: String = "https://api.deepseek.com",
  val modelName: String = DeepSeekClient.DEFAULT_MODEL,
  val availableModels: List<String> = emptyList(),
  val modelsBaseUrl: String? = null,
  val isLoadingModels: Boolean = false,
  val modelLoadError: AiChatError? = null,
  val error: AiChatError? = null,
)

enum class AiChatError {
  MissingApiKey,
  InvalidApiKey,
  RateLimited,
  InsufficientCredits,
  InvalidRequest,
  ServiceUnavailable,
  ServiceError,
  EmptyResponse,
  Network,
  SecureConnection,
}
