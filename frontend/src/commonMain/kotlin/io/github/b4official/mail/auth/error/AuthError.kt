package io.github.b4official.mail.auth.error

import io.github.b4official.mail.auth.model.LoginIdentifierError
import io.github.b4official.mail.auth.model.LoginRequestError

sealed interface AuthError {

    sealed interface LoginError : AuthError {
        data class InvalidIdentifier(val cause: LoginIdentifierError) : LoginError
        data class InvalidRequest(val cause: LoginRequestError) : LoginError

        // transport layer
        data class BadRequest(val statusCode: Int = 400, val message: String? = null) : LoginError
        data class InvalidCredentials(val statusCode: Int = 401, val message: String? = null) : LoginError
        data class AccessDenied(val statusCode: Int = 403, val message: String? = null) : LoginError
        data class RateLimited(val statusCode: Int = 429, val message: String? = null) : LoginError
        data class ServerError(val statusCode: Int, val message: String? = null) : LoginError
        data class UnexpectedResponse(val statusCode: Int, val message: String? = null) : LoginError
        data class Timeout(val cause: Throwable) : LoginError
        data class Network(val cause: Throwable) : LoginError
    }
}
