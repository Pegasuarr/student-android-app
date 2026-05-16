package com.example.frontend.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = PrimaryColor,
    onPrimary        = WhiteText,
    primaryContainer = PrimaryLight,
    secondary        = SecondaryColor,
    onSecondary      = WhiteText,
    background       = BackgroundColor,
    onBackground     = TextPrimaryColor,
    surface          = SurfaceColor,
    onSurface        = TextPrimaryColor,
    error            = ErrorColor,
    onError          = WhiteText,
)

@Composable
fun FrontendTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color(0xFFF0F4FF).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}