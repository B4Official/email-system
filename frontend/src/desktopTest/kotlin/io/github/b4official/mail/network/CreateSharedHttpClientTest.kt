package io.github.b4official.mail.network

import io.github.b4official.mail.config.AppClientMetadata
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateSharedHttpClientTest {
    @Test
    fun parseHttpLogLevelMapsKnownValues() {
        assertEquals(io.ktor.client.plugins.logging.LogLevel.ALL, parseHttpLogLevel("all"))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.HEADERS, parseHttpLogLevel("HEADERS"))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.BODY, parseHttpLogLevel(" body "))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.INFO, parseHttpLogLevel("INFO"))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.NONE, parseHttpLogLevel("none"))
    }

    @Test
    fun parseHttpLogLevelFallsBackToNoneForUnknownValues() {
        assertEquals(io.ktor.client.plugins.logging.LogLevel.NONE, parseHttpLogLevel(null))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.NONE, parseHttpLogLevel(""))
        assertEquals(io.ktor.client.plugins.logging.LogLevel.NONE, parseHttpLogLevel("unexpected"))
    }

    @Test
    fun sharedClientAddsAcceptAndUserAgentHeadersByDefault() = runTest {
        var capturedAccept: String? = null
        var capturedUserAgent: String? = null
        val metadata = AppClientMetadata(
            clientName = "email-system-desktop",
            versionName = "1.0.0-test",
            platformName = "desktop",
        )

        val client = createSharedHttpClient(
            engineFactory = MockEngine,
            clientMetadata = metadata,
            configuredLogLevelEnv = "NONE",
        ) {
            engine {
                addHandler { request ->
                    capturedAccept = request.headers[HttpHeaders.Accept]
                    capturedUserAgent = request.headers[HttpHeaders.UserAgent]

                    respond(
                        content = "{}",
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        ),
                    )
                }
            }
        }

        try {
            client.get("https://example.com/ping")
            assertEquals(ContentType.Application.Json.toString(), capturedAccept)
            assertEquals("email-system-desktop/1.0.0-test (desktop)", capturedUserAgent)
        } finally {
            client.close()
        }
    }
}
