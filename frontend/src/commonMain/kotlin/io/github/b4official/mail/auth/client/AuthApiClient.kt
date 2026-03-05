package io.github.b4official.mail.auth.client

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.context.bind
import arrow.core.raise.either
import arrow.core.right
import io.github.b4official.mail.auth.error.AuthError
import io.github.b4official.mail.auth.model.LoginIdentifier
import io.github.b4official.mail.auth.model.LoginResponse
import io.github.b4official.mail.auth.model.LoginRequest
import io.github.b4official.mail.auth.model.buildLoginRequest
import io.github.b4official.mail.auth.model.parseLoginIdentifier
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
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
    suspend fun login(username: String, password: String): Either<AuthError.LoginError, LoginResponse> = either {
        val request = buildLoginRequest(
            identifier = LoginIdentifier.Username(username),
            password = password,
        ).mapLeft(AuthError.LoginError::InvalidRequest).bind()
        login(request).bind()
    }

    suspend fun loginWithIdentifier(identifier: String, password: String): Either<AuthError.LoginError, LoginResponse> = either {
        val parsedIdentifier = parseLoginIdentifier(identifier)
            .mapLeft(AuthError.LoginError::InvalidIdentifier)
            .bind()
        val request = buildLoginRequest(parsedIdentifier, password)
            .mapLeft(AuthError.LoginError::InvalidRequest)
            .bind()
        login(request).bind()
    }

    suspend fun login(request: LoginRequest): Either<AuthError.LoginError, LoginResponse> =
        try {
            val response = client.post("$backendBaseUrl/api/auth/login") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                runCatching {
                    response.body<LoginResponse>()
                }.getOrElse { error ->
                    logger.warn(error) {
                        "LoginError failed to parse successful response with HTTP status ${response.status.value}"
                    }
                    return AuthError.LoginError.UnexpectedResponse(
                        statusCode = response.status.value,
                        message = error.message,
                    ).left()
                }.right()
            } else {
                val responseBody = response.bodyAsText()
                mapHttpFailure(response.status, responseBody).left()
            }
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            mapThrowableFailure(t).left()
        }

    private fun mapHttpFailure(status: HttpStatusCode, body: String?): AuthError.LoginError {
        val failure = when (status.value) {
            400 -> AuthError.LoginError.BadRequest(message = body)
            401 -> AuthError.LoginError.InvalidCredentials(message = body)
            403 -> AuthError.LoginError.AccessDenied(message = body)
            429 -> AuthError.LoginError.RateLimited(message = body)
            in 500..599 -> AuthError.LoginError.ServerError(statusCode = status.value, message = body)
            else -> AuthError.LoginError.UnexpectedResponse(statusCode = status.value, message = body)
        }
        logger.warn {
            "LoginError failed with HTTP status ${status.value} mapped to ${failure::class.simpleName}"
        }
        return failure
    }

    private fun mapThrowableFailure(throwable: Throwable): AuthError.LoginError {
        return if (looksLikeTimeout(throwable)) {
            AuthError.LoginError.Timeout(throwable)
        } else {
            AuthError.LoginError.Network(throwable)
        }
    }

    private fun looksLikeTimeout(throwable: Throwable): Boolean {
        val classNameContainsTimeout = throwable::class.simpleName
            ?.contains(other = "timeout", ignoreCase = true)
            ?: false
        val messageContainsTimeout = throwable.message
            ?.contains(other = "timeout", ignoreCase = true)
            ?: false

        return classNameContainsTimeout || messageContainsTimeout
    }
}
