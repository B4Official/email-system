package io.github.b4official.mail.auth.data

import io.github.b4official.mail.auth.data.model.LoginRequest
import io.github.b4official.mail.auth.data.model.LoginResult
import io.github.b4official.mail.auth.data.model.mapLoginFailureFromResponseStatus
import io.github.b4official.mail.auth.data.model.mapLoginFailureFromThrowable
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException

private val logger = KotlinLogging.logger {}

class AuthApiClient(
    private val client: HttpClient,
    private val backendBaseUrl: String,
) {
    suspend fun login(username: String, password: String): LoginResult {
        return login(
            LoginRequest(
                username = username,
                password = password,
            )
        )
    }

    suspend fun login(request: LoginRequest): LoginResult {
        return try {
            val response = client.post("$backendBaseUrl/api/auth/login") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                LoginResult.Success
            } else {
                mapHttpFailure(status = response.status, username = request.username)
            }
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            val failure = mapLoginFailureFromThrowable(throwable)
            logger.error(throwable = throwable) {
                "Login request failed for user '${request.username}' with reason '${failure.reason}'"
            }
            LoginResult.Failure(failure)
        }
    }

    private fun mapHttpFailure(status: HttpStatusCode, username: String): LoginResult.Failure {
        val failure = mapLoginFailureFromResponseStatus(status)
        logger.warn {
            "Login failed for user '$username' with HTTP status ${status.value} mapped to '${failure.reason}'"
        }
        return LoginResult.Failure(failure)
    }
}
