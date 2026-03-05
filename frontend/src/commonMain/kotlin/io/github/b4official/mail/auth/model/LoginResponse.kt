package io.github.b4official.mail.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val userId: String? = null,
    val username: String? = null,
)
