package io.github.b4official.mail.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

fun createSharedHttpClient(): HttpClient {
    return HttpClient(CIO) {
        // TODO: Configure timeouts, logging, auth headers, and JSON serialization.
    }
}
