package io.github.b4official.mail

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class EmailColors(
    val background: Color,
    val surface: Color,
    val text: Color,
    val textMuted: Color,
    val primary: Color,
    val primaryText: Color,
    val border: Color,
    val danger: Color,
)

@Immutable
data class EmailTypography(
    val title: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
)

@Immutable
data class EmailShapes(
    val cardRadius: androidx.compose.foundation.shape.CornerSize,
    val fieldRadius: androidx.compose.foundation.shape.CornerSize,
    val buttonRadius: androidx.compose.foundation.shape.CornerSize,
)

@Stable
class EmailThemeTokens(
    val colors: EmailColors,
    val typography: EmailTypography,
    val shapes: EmailShapes,
    val spacing: EmailSpacing,
)

@Immutable
data class EmailSpacing(
    val xs: androidx.compose.ui.unit.Dp = 6.dp,
    val sm: androidx.compose.ui.unit.Dp = 10.dp,
    val md: androidx.compose.ui.unit.Dp = 16.dp,
    val lg: androidx.compose.ui.unit.Dp = 24.dp,
)

private val LocalEmailTheme = staticCompositionLocalOf<EmailThemeTokens> {
    error("EmailTheme not provided")
}

object EmailTheme {
    val colors: EmailColors @Composable get() = LocalEmailTheme.current.colors
    val typography: EmailTypography @Composable get() = LocalEmailTheme.current.typography
    val shapes: EmailShapes @Composable get() = LocalEmailTheme.current.shapes
    val spacing: EmailSpacing @Composable get() = LocalEmailTheme.current.spacing
}

private val Light = EmailColors(
    background = Color(0xFFF7F7FA),
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF101114),
    textMuted = Color(0xFF5A5F6A),
    primary = Color(0xFF3B82F6),
    primaryText = Color(0xFFFFFFFF),
    border = Color(0xFFE3E5EA),
    danger = Color(0xFFEF4444),
)

private val Dark = EmailColors(
    background = Color(0xFF0B0C0F),
    surface = Color(0xFF12141A),
    text = Color(0xFFF4F5F7),
    textMuted = Color(0xFFA4AAB6),
    primary = Color(0xFF60A5FA),
    primaryText = Color(0xFF0B0C0F),
    border = Color(0xFF2A2F3A),
    danger = Color(0xFFF87171),
)

private val Typo = EmailTypography(
    title = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    body = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal),
    label = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)

private val Shapes = EmailShapes(
    cardRadius = androidx.compose.foundation.shape.CornerSize(16.dp),
    fieldRadius = androidx.compose.foundation.shape.CornerSize(12.dp),
    buttonRadius = androidx.compose.foundation.shape.CornerSize(12.dp),
)

@Composable
fun EmailTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val tokens = remember(useDarkTheme) {
        EmailThemeTokens(
            colors = if (useDarkTheme) Dark else Light,
            typography = Typo,
            shapes = Shapes,
            spacing = EmailSpacing(),
        )
    }

    CompositionLocalProvider(LocalEmailTheme provides tokens) {
        content()
    }
}
