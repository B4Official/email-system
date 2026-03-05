package io.github.b4official.mail.feature.login

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
import io.github.b4official.mail.ui.theme.EmailTheme

data class LoginUiState(
    val identifier: String,
    val password: String,
    val identifierError: Boolean,
    val passwordError: Boolean,
    val canSubmit: Boolean,
)


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (identifier: String, password: String) -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var attempted by remember { mutableStateOf(false) }

    val identifierError = attempted && identifier.isBlank()
    val passwordError = attempted && password.isBlank()
    val canSubmit = identifier.isNotBlank() && password.isNotBlank()

    val state = LoginUiState(
        identifier,
        password,
        identifierError,
        passwordError,
        canSubmit,
    )

    LoginContent(
        modifier,
        state,
        onIdentifierChange = { newIdentifier -> identifier = newIdentifier },
        onPasswordChange = { newPassword -> password = newPassword },
        onSubmit = { onLogin(identifier, password) },
    )
}

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    onIdentifierChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val c = EmailTheme.colors
    val sp = EmailTheme.spacing

    Box(
        modifier
            .fillMaxSize()
            .background(c.background)
            .padding(sp.lg),
        contentAlignment = Alignment.Center
    ) {
        LoginCard(
            modifier = Modifier,
            state,
            onIdentifierChange,
            onPasswordChange,
            onSubmit
        )
    }
}

@Composable
private fun LoginCard(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    onIdentifierChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val c = EmailTheme.colors
    val s = EmailTheme.shapes
    val sp = EmailTheme.spacing

    Column(
        modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(s.cardRadius))
            .background(c.surface)
            .border(1.dp, c.border, RoundedCornerShape(s.cardRadius))
            .padding(sp.lg),
        verticalArrangement = Arrangement.spacedBy(sp.md)
    ) {
        LoginHeader()
        IdentifierField(state.identifier, onIdentifierChange, state.identifierError)
        PasswordField(state.password, onPasswordChange, state.passwordError)
        LoginButton(state.canSubmit, onSubmit)
    }
}

@Composable
private fun LoginHeader() {
    val c = EmailTheme.colors
    val t = EmailTheme.typography

    BasicText(
        text = "Sign in",
        style = t.title.copy(color = c.text),
    )

    BasicText(
        text = "Use your username or email, plus password.",
        style = t.body.copy(color = c.text),
    )
}

@Composable
private fun IdentifierField(identifier: String, onValueChange: (String) -> Unit, identifierError: Boolean) {
    LabeledField(
        label = "Username or Email",
        value = identifier,
        onValueChange,
        isError = identifierError,
        errorText = "Username or email is required",
        keyboardType = KeyboardType.Text,
    )

}

@Composable
private fun PasswordField(password: String, onValueChange: (String) -> Unit, passwordError: Boolean) {
    LabeledField(
        label = "Password",
        value = password,
        onValueChange,
        isError = passwordError,
        errorText = "Password is required",
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    )

}

@Composable
private fun LoginButton(canSubmit: Boolean, onClick: () -> Unit) {
    val c = EmailTheme.colors
    val t = EmailTheme.typography
    val s = EmailTheme.shapes
    val sp = EmailTheme.spacing

    val shape = RoundedCornerShape(s.buttonRadius)

    val text = "Log In"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape)
            .background(if (canSubmit) c.primary else c.border)
            .clickable(enabled = canSubmit, onClick = onClick)
            .padding(horizontal = sp.md),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = t.body.copy(color = if (canSubmit) c.primaryText else c.textMuted),
        )
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
