package io.github.b4official.mail.auth.data.model

import io.ktor.http.ContentType
import io.ktor.http.content.TextContent

data class LoginRequest(
    val username: String,
    val password: String,
) {
    fun toRequestBody(): TextContent {
        val jsonBody = """
            {
              "username": "${username.escapeJson()}",
              "password": "${password.escapeJson()}"
            }
        """.trimIndent()

        return TextContent(
            text = jsonBody,
            contentType = ContentType.Application.Json,
        )
    }
}

private fun String.escapeJson(): String {
    return buildString(length) {
        for (character in this@escapeJson) {
            when (character) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(character)
            }
        }
    }
}
