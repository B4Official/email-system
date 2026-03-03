package io.github.b4official.mail

expect class Platform {
    val name: String
}

expect fun getPlatform(): Platform
