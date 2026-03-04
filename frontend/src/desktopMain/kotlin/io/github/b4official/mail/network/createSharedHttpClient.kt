package io.github.b4official.mail.network

import io.github.b4official.mail.config.AppClientMetadata
import io.github.b4official.mail.config.loadAppClientMetadata
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val HTTP_LOG_LEVEL_ENV = "EMAIL_HTTP_LOG_LEVEL"

fun createSharedHttpClient(): HttpClient {
    return createSharedHttpClient(
        engineFactory = CIO,
        clientMetadata = loadAppClientMetadata(),
        configuredLogLevelEnv = System.getenv(HTTP_LOG_LEVEL_ENV),
    )
}

internal fun <T : HttpClientEngineConfig> createSharedHttpClient(
    engineFactory: HttpClientEngineFactory<T>,
    clientMetadata: AppClientMetadata,
    configuredLogLevelEnv: String? = System.getenv(HTTP_LOG_LEVEL_ENV),
    additionalConfiguration: HttpClientConfig<T>.() -> Unit = {},
): HttpClient {
    return HttpClient(engineFactory) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }

        install(Logging) {
            level = parseHttpLogLevel(configuredLogLevelEnv)
        }

        defaultRequest {
            accept(ContentType.Application.Json)
            header(HttpHeaders.UserAgent, clientMetadata.userAgent)
        }

        additionalConfiguration()
    }
}

internal fun parseHttpLogLevel(rawValue: String?): LogLevel {
    val raw = rawValue?.trim()?.uppercase()
    return when (raw) {
        "ALL" -> LogLevel.ALL
        "HEADERS" -> LogLevel.HEADERS
        "BODY" -> LogLevel.BODY
        "INFO" -> LogLevel.INFO
        "NONE" -> LogLevel.NONE
        else -> LogLevel.NONE
    }
}
