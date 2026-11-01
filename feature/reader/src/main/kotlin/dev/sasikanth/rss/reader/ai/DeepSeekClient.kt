package dev.sasikanth.rss.reader.ai

import dev.sasikanth.rss.reader.di.scopes.AppScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.net.ssl.SSLException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class DeepSeekClient(private val httpClient: HttpClient) {

  suspend fun listModels(baseUrl: String, apiKey: String): List<String> {
    try {
      val response =
        httpClient.get(endpoint(baseUrl, "/models")) {
          header(HttpHeaders.Authorization, "Bearer $apiKey")
        }
      if (response.status.value !in 200..299) {
        throw classifyDeepSeekFailure(response.status.value, response.bodyAsText())
      }
      return response.body<DeepSeekModelsResponse>().data.map { it.id }.filter(String::isNotBlank)
    } catch (exception: ResponseException) {
      val body = runCatching { exception.response.bodyAsText() }.getOrDefault("")
      throw classifyDeepSeekFailure(exception.response.status.value, body)
    } catch (exception: SSLException) {
      throw DeepSeekException.SecureConnection
    }
  }

  suspend fun complete(
    baseUrl: String,
    apiKey: String,
    model: String,
    messages: List<DeepSeekMessage>,
  ): String {
    try {
      val response =
        httpClient.post(endpoint(baseUrl, "/chat/completions")) {
          header(HttpHeaders.Authorization, "Bearer $apiKey")
          contentType(ContentType.Application.Json)
          setBody(
            DeepSeekRequest(
              model = model,
              messages = messages,
              stream = false,
            )
          )
        }

      if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.Forbidden) {
        throw DeepSeekException.InvalidKey
      }
      if (response.status == HttpStatusCode.TooManyRequests) {
        throw DeepSeekException.RateLimited
      }
      if (response.status.value !in 200..299) {
        throw classifyDeepSeekFailure(response.status.value, response.bodyAsText())
      }

      return response.body<DeepSeekResponse>().choices.firstOrNull()?.message?.content
        ?.takeIf(String::isNotBlank)
        ?: throw DeepSeekException.EmptyResponse
    } catch (exception: ResponseException) {
      val body = runCatching { exception.response.bodyAsText() }.getOrDefault("")
      throw classifyDeepSeekFailure(exception.response.status.value, body)
    } catch (exception: SSLException) {
      throw DeepSeekException.SecureConnection
    }
  }

  private fun endpoint(baseUrl: String, apiPath: String): String {
    val normalized = baseUrl.trim().trimEnd('/')
    require(normalized.startsWith("https://", ignoreCase = true)) {
      "Base URL must use HTTPS"
    }
    val withoutChatEndpoint = normalized.removeSuffixIgnoreCase("/chat/completions")
    val withoutModelsEndpoint = withoutChatEndpoint.removeSuffixIgnoreCase("/models")
    return "$withoutModelsEndpoint$apiPath"
  }

  private fun String.removeSuffixIgnoreCase(suffix: String): String =
    if (endsWith(suffix, ignoreCase = true)) dropLast(suffix.length) else this

  companion object {
    const val DEFAULT_MODEL = "deepseek-flash"
  }
}

sealed class DeepSeekException : Exception() {
  data object InvalidKey : DeepSeekException()
  data object RateLimited : DeepSeekException()
  data object InsufficientCredits : DeepSeekException()
  data object InvalidRequest : DeepSeekException()
  data object ServiceUnavailable : DeepSeekException()
  data class ServiceError(val statusCode: Int) : DeepSeekException()
  data object EmptyResponse : DeepSeekException()
  data object SecureConnection : DeepSeekException()
}

internal fun classifyDeepSeekFailure(statusCode: Int, responseBody: String): DeepSeekException {
  val errorCode =
    runCatching {
      Json.parseToJsonElement(responseBody)
        .jsonObject["error"]
        ?.jsonObject
        ?.get("code")
        ?.jsonPrimitive
        ?.content
        .orEmpty()
    }
      .getOrDefault("")
      .lowercase()

  return when {
    statusCode == 401 || statusCode == 403 -> DeepSeekException.InvalidKey
    statusCode == 402 || errorCode.contains("insufficient_balance") ||
      errorCode.contains("insufficient_quota") -> DeepSeekException.InsufficientCredits
    statusCode == 429 -> DeepSeekException.RateLimited
    statusCode == 400 || statusCode == 422 -> DeepSeekException.InvalidRequest
    statusCode >= 500 -> DeepSeekException.ServiceUnavailable
    else -> DeepSeekException.ServiceError(statusCode)
  }
}
