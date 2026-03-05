package io.github.b4official.mail.auth.data

import arrow.core.Either
import io.github.b4official.mail.auth.client.AuthApiClient
import io.github.b4official.mail.auth.error.AuthError
import io.github.b4official.mail.auth.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthApiClientTest {
    @Test
    fun loginReturnsSuccessOn2xxResponse() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.OK)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "top-secret")
            val success = result.expectRight()
            assertEquals(LoginResponse(), success)
        }
    }

    @Test
    fun loginMaps401ToInvalidCredentialsFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.Unauthorized)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "wrong-pass")
            val failure = result.expectLeft()
            val invalidCredentials = assertIs<AuthError.LoginError.InvalidCredentials>(failure)
            assertEquals(401, invalidCredentials.statusCode)
        }
    }

    @Test
    fun loginMaps429ToRateLimitedFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.TooManyRequests)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "top-secret")
            val failure = result.expectLeft()
            val rateLimited = assertIs<AuthError.LoginError.RateLimited>(failure)
            assertEquals(429, rateLimited.statusCode)
        }
    }

    @Test
    fun loginMaps5xxToServerErrorFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.InternalServerError)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "top-secret")
            val failure = result.expectLeft()
            val serverError = assertIs<AuthError.LoginError.ServerError>(failure)
            assertEquals(500, serverError.statusCode)
        }
    }

    @Test
    fun loginMapsTimeoutLikeErrorsToTimeoutFailure() = runTest {
        val httpClient = createMockClient(throwable = IllegalStateException("Request timeout while connecting"))
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "top-secret")
            val failure = result.expectLeft()
            assertIs<AuthError.LoginError.Timeout>(failure)
        }
    }

    @Test
    fun loginMapsGenericTransportErrorsToNetworkFailure() = runTest {
        val httpClient = createMockClient(throwable = IllegalStateException("Connection reset by peer"))
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        httpClient.use { httpClient ->
            val result = subject.login("alice", "top-secret")
            val failure = result.expectLeft()
            assertIs<AuthError.LoginError.Network>(failure)
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

private fun <L, R> Either<L, R>.expectRight(): R {
    return when (this) {
        is Either.Right -> value
        is Either.Left -> error("Expected Either.Right but got Either.Left($value)")
    }
}

private fun <L, R> Either<L, R>.expectLeft(): L {
    return when (this) {
        is Either.Left -> value
        is Either.Right -> error("Expected Either.Left but got Either.Right($value)")
    }
}
