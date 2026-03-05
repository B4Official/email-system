package io.github.b4official.mail.auth.model

import arrow.core.Either
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class LoginErrorRequestFactoryTest {
    @Test
    fun buildLoginRequestMapsUsernameIdentifierToUsernamePayload() {
        val request = assertIs<Either.Right<LoginRequest>>(
            buildLoginRequest(
                identifier = LoginIdentifier.Username("alice"),
                password = "top-secret",
            )
        ).value

        assertEquals("alice", request.username)
        assertNull(request.email)
        assertEquals("top-secret", request.password)
    }

    @Test
    fun buildLoginRequestMapsEmailIdentifierToEmailPayload() {
        val request = assertIs<Either.Right<LoginRequest>>(
            buildLoginRequest(
                identifier = LoginIdentifier.Email("alice@example.com"),
                password = "top-secret",
            )
        ).value

        assertNull(request.username)
        assertEquals("alice@example.com", request.email)
        assertEquals("top-secret", request.password)
    }

    @Test
    fun buildLoginRequestRejectsBlankPassword() {
        val failure = assertIs<Either.Left<LoginRequestError>>(
            buildLoginRequest(
                identifier = LoginIdentifier.Username("alice"),
                password = "   ",
            )
        ).value
        assertIs<LoginRequestError.BlankPassword>(failure)
    }
}
