package io.github.b4official.mail.auth.data

import arrow.core.Either
import io.github.b4official.mail.auth.client.AuthApiClient
import io.github.b4official.mail.auth.error.AuthError
import io.github.b4official.mail.auth.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthApiClientIdentifierLoginErrorTest {
    @Test
    fun loginWithIdentifierSupportsUsernameInputs() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.OK)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { _ ->
            val result = subject.loginWithIdentifier("alice", "top-secret")
            val success = assertIs<Either.Right<LoginResponse>>(result).value
            assertEquals(LoginResponse(), success)
        }
    }

    @Test
    fun loginWithIdentifierSupportsEmailInputs() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.OK)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { _ ->
            val result = subject.loginWithIdentifier("alice@example.com", "top-secret")
            val success = assertIs<Either.Right<LoginResponse>>(result).value
            assertEquals(LoginResponse(), success)
        }
    }

    @Test
    fun loginWithIdentifierMaps401ToInvalidCredentialsFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.Unauthorized)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { _ ->
            val result = subject.loginWithIdentifier("alice@example.com", "wrong-pass")
            val failure = assertIs<Either.Left<AuthError.LoginError>>(result).value
            val invalidCredentials = assertIs<AuthError.LoginError.InvalidCredentials>(failure)
            assertEquals(401, invalidCredentials.statusCode)
        }
    }

    private fun createMockClient(
        status: HttpStatusCode? = null,
        throwable: Throwable? = null,
    ): HttpClient {
        return HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            engine {
                addHandler {
                    if (throwable != null) {
                        throw throwable
                    }

                    respond(
                        content = "{}",
                        status = status ?: HttpStatusCode.OK,
                        headers = headersOf(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        ),
                    )
                }
            }
        }
    }

    companion object {
        private const val TEST_BASE_URL = "http://localhost:8080"
    }
}
