package io.github.b4official.mail.auth.data.model

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String,
)

sealed interface LoginRequestError {
    object BlankPassword : LoginRequestError
}

fun buildLoginRequest(identifier: LoginIdentifier, password: String): Either<LoginRequestError, LoginRequest> {
    if (password.isBlank()) return LoginRequestError.BlankPassword.left()

    return when (identifier) {
        is LoginIdentifier.Email -> {
            LoginRequest(
                email = identifier.value,
                username = null,
                password = password
            ).right()
        }

        is LoginIdentifier.Username -> {
            LoginRequest(
                email = null,
                username = identifier.value,
                password = password
            ).right()
        }
    }
}
