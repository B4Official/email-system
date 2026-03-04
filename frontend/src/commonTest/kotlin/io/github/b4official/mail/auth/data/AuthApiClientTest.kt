package io.github.b4official.mail.auth.data

import io.github.b4official.mail.auth.data.model.LoginFailureReason
import io.github.b4official.mail.auth.data.model.LoginResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthApiClientTest {
    @Test
    fun loginReturnsSuccessOn2xxResponse() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.OK)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "top-secret")
            assertIs<LoginResult.Success>(result)
        } finally {
            httpClient.close()
        }
    }

    @Test
    fun loginMaps401ToInvalidCredentialsFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.Unauthorized)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "wrong-pass")
            val failure = assertIs<LoginResult.Failure>(result).failure
            assertEquals(LoginFailureReason.INVALID_CREDENTIALS, failure.reason)
            assertEquals(401, failure.statusCode)
        } finally {
            httpClient.close()
        }
    }

    @Test
    fun loginMaps429ToRateLimitedFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.TooManyRequests)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "top-secret")
            val failure = assertIs<LoginResult.Failure>(result).failure
            assertEquals(LoginFailureReason.RATE_LIMITED, failure.reason)
            assertEquals(429, failure.statusCode)
        } finally {
            httpClient.close()
        }
    }

    @Test
    fun loginMaps5xxToServerErrorFailure() = runTest {
        val httpClient = createMockClient(status = HttpStatusCode.InternalServerError)
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "top-secret")
            val failure = assertIs<LoginResult.Failure>(result).failure
            assertEquals(LoginFailureReason.SERVER_ERROR, failure.reason)
            assertEquals(500, failure.statusCode)
        } finally {
            httpClient.close()
        }
    }

    @Test
    fun loginMapsTimeoutLikeErrorsToTimeoutFailure() = runTest {
        val httpClient = createMockClient(throwable = IllegalStateException("Request timeout while connecting"))
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "top-secret")
            val failure = assertIs<LoginResult.Failure>(result).failure
            assertEquals(LoginFailureReason.TIMEOUT, failure.reason)
        } finally {
            httpClient.close()
        }
    }

    @Test
    fun loginMapsGenericTransportErrorsToNetworkFailure() = runTest {
        val httpClient = createMockClient(throwable = IllegalStateException("Connection reset by peer"))
        val subject = AuthApiClient(httpClient, TEST_BASE_URL)

        try {
            val result = subject.login("alice", "top-secret")
            val failure = assertIs<LoginResult.Failure>(result).failure
            assertEquals(LoginFailureReason.NETWORK_ERROR, failure.reason)
        } finally {
            httpClient.close()
        }
    }

    private fun createMockClient(
        status: HttpStatusCode? = null,
        throwable: Throwable? = null,
    ): HttpClient {
        return HttpClient(MockEngine) {
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
