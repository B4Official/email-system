package io.github.b4official.mail

import androidx.compose.runtime.Composable

@Composable
fun App() {
    EmailTheme(useDarkTheme = true) {
        LoginScreen { username, password ->
            println("Username $username, password $password")
            // call your auth logic here
        }
    }
}