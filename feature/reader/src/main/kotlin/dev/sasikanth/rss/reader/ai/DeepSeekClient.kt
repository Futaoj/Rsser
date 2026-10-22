package dev.sasikanth.rss.reader.ai

import dev.sasikanth.rss.reader.di.scopes.AppScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class DeepSeekClient(private val httpClient: HttpClient) {

  suspend fun complete(apiKey: String, messages: List<DeepSeekMessage>): String {
    try {
      val response =
        httpClient.post(ENDPOINT) {
          header(HttpHeaders.Authorization, "Bearer $apiKey")
          contentType(ContentType.Application.Json)
          setBody(DeepSeekRequest(messages = messages))
        }

      if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.Forbidden) {
        throw DeepSeekException.InvalidKey
      }
      if (response.status == HttpStatusCode.TooManyRequests) {
        throw DeepSeekException.RateLimited
      }
      if (response.status.value !in 200..299) {
        throw DeepSeekException.ServiceError
      }

      return response.body<DeepSeekResponse>().choices.firstOrNull()?.message?.content
        ?.takeIf(String::isNotBlank)
        ?: throw DeepSeekException.EmptyResponse
    } catch (exception: ResponseException) {
      throw when (exception.response.status) {
        HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> DeepSeekException.InvalidKey
        HttpStatusCode.TooManyRequests -> DeepSeekException.RateLimited
        else -> DeepSeekException.ServiceError
      }
    }
  }

  companion object {
    const val MODEL = "deepseek-flash"
    private const val ENDPOINT = "https://api.deepseek.com/chat/completions"
  }
}

sealed class DeepSeekException : Exception() {
  data object InvalidKey : DeepSeekException()
  data object RateLimited : DeepSeekException()
  data object ServiceError : DeepSeekException()
  data object EmptyResponse : DeepSeekException()
}
