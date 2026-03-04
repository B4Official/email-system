package io.github.b4official.mail.config

import io.github.b4official.mail.BuildConfig

private const val BACKEND_URL_ENV_VAR = "EMAIL_BACKEND_URL"

fun loadAppConfig(): AppConfig {
    val configuredBaseUrl = System.getenv(BACKEND_URL_ENV_VAR)
        ?.trim()
        ?.trimEnd('/')
        .takeUnless { it.isNullOrBlank() }
        ?: BuildConfig.BACKEND_BASE_URL.trimEnd('/')

    return AppConfig(
        backendBaseUrl = configuredBaseUrl,
    )
}
