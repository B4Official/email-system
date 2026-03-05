package io.github.b4official.mail.auth.model

import arrow.core.Either
import arrow.core.left
import arrow.core.right

sealed interface LoginIdentifier {
    val value: String

    data class Username(override val value: String) : LoginIdentifier
    data class Email(override val value: String) : LoginIdentifier
}

sealed interface LoginIdentifierError {
    object Blank : LoginIdentifierError

    data class InvalidEmail(
        val input: String
    ) : LoginIdentifierError

    data class InvalidUsername(
        val input: String,
        val reason: UsernameFailureReason
    ) : LoginIdentifierError
}

enum class UsernameFailureReason {
    TOO_SHORT,
    TOO_LONG,
    INVALID_CHARACTERS
}

fun validateEmail(email: String): Either<LoginIdentifierError.InvalidEmail, String> {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    if (email.matches(regex)) {
        return email.right()
    }
    return LoginIdentifierError.InvalidEmail(email).left()
}

fun validateUsername(username: String): Either<LoginIdentifierError.InvalidUsername, String> {
    if (username.length < 3) {
        return LoginIdentifierError.InvalidUsername(
            username,
            UsernameFailureReason.TOO_SHORT
        ).left()
    }

    if (username.length > 32) {
        return LoginIdentifierError.InvalidUsername(
            username,
            UsernameFailureReason.TOO_LONG
        ).left()
    }

    if (!username.all { it.isLetterOrDigit() || it == '_' || it == '.' }) {
        return LoginIdentifierError.InvalidUsername(
            username,
            UsernameFailureReason.INVALID_CHARACTERS
        ).left()
    }

    return username.right()
}

fun parseLoginIdentifier(rawIdentifier: String): Either<LoginIdentifierError, LoginIdentifier> {
    val identifier = rawIdentifier.trim().lowercase()
    if (identifier.isBlank()) return LoginIdentifierError.Blank.left()

    return if ('@' in identifier) {
        validateEmail(identifier).map(LoginIdentifier::Email)
    } else {
        validateUsername(identifier).map(LoginIdentifier::Username)
    }
}
