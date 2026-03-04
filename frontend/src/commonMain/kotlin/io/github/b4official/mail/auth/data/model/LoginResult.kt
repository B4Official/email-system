package io.github.b4official.mail.auth.data.model

import io.ktor.http.HttpStatusCode

sealed interface LoginResult {
    data object Success : LoginResult

    data class Failure(
        val failure: LoginFailure,
    ) : LoginResult
}

data class LoginFailure(
    val reason: LoginFailureReason,
    val statusCode: Int? = null,
    val message: String? = null,
)

enum class LoginFailureReason {
    INVALID_REQUEST,
    INVALID_CREDENTIALS,
    ACCESS_DENIED,
    RATE_LIMITED,
    SERVER_ERROR,
    TIMEOUT,
    NETWORK_ERROR,
    UNEXPECTED_RESPONSE,
}

internal fun mapLoginFailureFromResponseStatus(status: HttpStatusCode): LoginFailure {
    val reason = when (status.value) {
        400 -> LoginFailureReason.INVALID_REQUEST
        401 -> LoginFailureReason.INVALID_CREDENTIALS
        403 -> LoginFailureReason.ACCESS_DENIED
        429 -> LoginFailureReason.RATE_LIMITED
        in 500..599 -> LoginFailureReason.SERVER_ERROR
        else -> LoginFailureReason.UNEXPECTED_RESPONSE
    }

    return LoginFailure(
        reason = reason,
        statusCode = status.value,
        message = "Login failed with HTTP status ${status.value}",
    )
}

internal fun mapLoginFailureFromThrowable(throwable: Throwable): LoginFailure {
    val reason = if (looksLikeTimeout(throwable)) {
        LoginFailureReason.TIMEOUT
    } else {
        LoginFailureReason.NETWORK_ERROR
    }

    return LoginFailure(
        reason = reason,
        message = throwable.message,
    )
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
