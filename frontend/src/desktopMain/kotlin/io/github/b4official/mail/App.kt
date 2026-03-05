package io.github.b4official.mail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import arrow.core.fold
import io.github.b4official.mail.auth.client.AuthApiClient
import io.github.b4official.mail.config.loadAppConfig
import io.github.b4official.mail.feature.login.LoginScreen
import io.github.b4official.mail.network.createSharedHttpClient
import io.github.b4official.mail.ui.theme.EmailTheme
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger {}

@Composable
fun App() {
    val config = remember { loadAppConfig() }
    val httpClient = remember { createSharedHttpClient() }
    val authApi = remember { AuthApiClient(httpClient, config.backendBaseUrl) }
    val scope = rememberCoroutineScope()

    DisposableEffect(httpClient) {
        onDispose {
            httpClient.close()
        }
    }

    EmailTheme(useDarkTheme = true) {
        LoginScreen { identifier, password ->
            scope.launch {
                val loginResult = authApi.loginWithIdentifier(identifier, password)
                loginResult.fold(
                    ifLeft = { error ->
                        val message = "identifier '$identifier' failed to log in"
                        logger.warn { "LoginError failed with ${error::class.simpleName}" }
                        logger.info { message }
                        println(message)
                    },
                    ifRight = {
                        val message = "identifier '$identifier' successfully logged in"
                        logger.info { message }
                        println(message)
                    }
                )
            }
        }
    }
}
