package io.github.b4official.mail.config

import io.github.b4official.mail.BuildConfig

data class AppClientMetadata(
    val clientName: String,
    val versionName: String,
    val platformName: String,
) {
    val userAgent: String
        get() = "$clientName/$versionName ($platformName)"
}

fun loadAppClientMetadata(): AppClientMetadata {
    return AppClientMetadata(
        clientName = BuildConfig.APP_CLIENT_NAME,
        versionName = BuildConfig.APP_VERSION_NAME,
        platformName = "desktop",
    )
}
