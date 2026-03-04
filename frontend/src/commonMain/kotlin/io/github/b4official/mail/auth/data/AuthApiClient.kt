package io.github.b4official.mail.auth.data

import io.github.b4official.mail.auth.data.model.LoginRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

private val logger = KotlinLogging.logger {}

class AuthApiClient(
    private val client: HttpClient,
    private val backendBaseUrl: String,
) {
    suspend fun login(username: String, password: String): Boolean {
        return login(
            LoginRequest(
                username = username,
                password = password,
            )
        )
    }

    suspend fun login(request: LoginRequest): Boolean {
        return runCatching {
            client.post("$backendBaseUrl/api/auth/login") {
                setBody(request.toRequestBody())
            }.status.isSuccess()
        }.getOrElse { throwable ->
            // TODO: Replace this boolean fallback with a typed result and structured error handling.
            logger.error(
                throwable = throwable,
            ) { "Login request failed for user '${request.username}'" }
            false
        }
    }
}
