package io.github.b4official.mail.auth.model

import arrow.core.Either
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginErrorIdentifierTest {
    @Test
    fun parseLoginIdentifierClassifiesUsername() {
        val identifier = parseLoginIdentifier("alice")

        val username = assertIs<LoginIdentifier.Username>(
            assertIs<Either.Right<LoginIdentifier>>(identifier).value
        )
        assertEquals("alice", username.value)
    }

    @Test
    fun parseLoginIdentifierClassifiesEmail() {
        val identifier = parseLoginIdentifier("alice@example.com")

        val email = assertIs<LoginIdentifier.Email>(
            assertIs<Either.Right<LoginIdentifier>>(identifier).value
        )
        assertEquals("alice@example.com", email.value)
    }

    @Test
    fun parseLoginIdentifierTrimsInputBeforeClassification() {
        val identifier = parseLoginIdentifier("   alice@example.com   ")

        val email = assertIs<LoginIdentifier.Email>(
            assertIs<Either.Right<LoginIdentifier>>(identifier).value
        )
        assertEquals("alice@example.com", email.value)
    }

    @Test
    fun parseLoginIdentifierRejectsBlankIdentifier() {
        val failure = assertIs<Either.Left<LoginIdentifierError>>(parseLoginIdentifier("   ")).value
        assertIs<LoginIdentifierError.Blank>(failure)
    }

    @Test
    fun parseLoginIdentifierRejectsMalformedEmailInput() {
        val failure = assertIs<Either.Left<LoginIdentifierError>>(parseLoginIdentifier("alice@")).value
        val invalidEmail = assertIs<LoginIdentifierError.InvalidEmail>(failure)
        assertEquals("alice@", invalidEmail.input)
    }
}
