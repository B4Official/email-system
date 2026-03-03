package io.github.b4official.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (username: String, password: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var attempted by remember { mutableStateOf(false) }

    val c = EmailTheme.colors
    val t = EmailTheme.typography
    val s = EmailTheme.shapes
    val sp = EmailTheme.spacing

    val usernameError = attempted && username.isBlank()
    val passwordError = attempted && password.isBlank()
    val canSubmit = username.isNotBlank() && password.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(c.background)
            .padding(sp.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(s.cardRadius))
                .background(c.surface)
                .border(1.dp, c.border, RoundedCornerShape(s.cardRadius))
                .padding(sp.lg),
            verticalArrangement = Arrangement.spacedBy(sp.md)
        ) {
            BasicText(
                text = "Sign in",
                style = t.title.copy(color = c.text),
            )

            BasicText(
                text = "Use your username and password.",
                style = t.body.copy(color = c.text),
            )

            LabeledField(
                label = "Username",
                value = username,
                onValueChange = { username = it },
                isError = usernameError,
                errorText = "Username is required",
                keyboardType = KeyboardType.Text,
            )

            LabeledField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                isError = passwordError,
                errorText = "Password is required",
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            PrimaryButton(
                text = "Sign in",
                enabled = canSubmit,
                onClick = {
                    attempted = true
                    if (!canSubmit) return@PrimaryButton
                    onLogin(username.trim(), password)
                }
            )
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val c = EmailTheme.colors
    val t = EmailTheme.typography
    val s = EmailTheme.shapes
    val sp = EmailTheme.spacing

    Column(verticalArrangement = Arrangement.spacedBy(sp.xs)) {
        BasicText(
            text = label,
            style = t.label.copy(color = c.textMuted),
        )

        val shape = RoundedCornerShape(s.fieldRadius)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(c.background)
                .border(
                    width = 1.dp,
                    color = if (isError) c.danger else c.border,
                    shape = shape
                )
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = c.text,
                    fontSize = t.body.fontSize,
                    fontWeight = t.body.fontWeight
                ),
                cursorBrush = SolidColor(c.primary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isError) {
            BasicText(
                text = errorText,
                style = t.label.copy(color = c.danger),
            )
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val c = EmailTheme.colors
    val t = EmailTheme.typography
    val s = EmailTheme.shapes
    val sp = EmailTheme.spacing

    val shape = RoundedCornerShape(s.buttonRadius)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape)
            .background(if (enabled) c.primary else c.border)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = sp.md),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = t.body.copy(color = if (enabled) c.primaryText else c.textMuted),
        )
    }
}

@Composable
fun LoginCard(modifier: Modifier = Modifier) {
    // TODO: Card container, elevation, shape.
    // TODO: Column layout for header, fields, button, footer.
}

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    // TODO: App name/logo.
    // TODO: Title and subtitle.
}

@Composable
fun UsernameField(modifier: Modifier = Modifier) {
    // TODO: Static username input UI (no state, no validation).
}

@Composable
fun PasswordField(modifier: Modifier = Modifier) {
    // TODO: Static password input UI (no state, no validation).
}

@Composable
fun LoginButton(modifier: Modifier = Modifier) {
    // TODO: Button UI only, no onClick.
}

@Composable
fun LoginFooter(modifier: Modifier = Modifier) {
    // TODO: Optional: “Forgot password?” / “Create account” text.
}
