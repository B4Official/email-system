package io.github.b4official.mail

import androidx.compose.runtime.Composable
import io.github.b4official.mail.feature.login.LoginScreen
import io.github.b4official.mail.ui.theme.EmailTheme

@Composable
fun App() {
    EmailTheme(useDarkTheme = true) {
        LoginScreen { username, password ->
            println("Username $username, password $password")
            // call auth logic here
        }
    }
}