package io.github.b4official.mail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.b4official.mail.auth.data.AuthApiClient
import io.github.b4official.mail.auth.data.model.LoginResult
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
        LoginScreen { username, password ->
            scope.launch {
                val loginResult = authApi.login(username, password)
                val outcome = if (loginResult is LoginResult.Success) "succesfully" else "fail to"
                val message = "username$username $outcome log in"

                if (loginResult is LoginResult.Failure) {
                    logger.warn { "Login failed with reason '${loginResult.failure.reason}'" }
                }
                logger.info { message }
                println(message)
            }
        }
    }
}
