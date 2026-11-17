package com.charahua.gestorcitassanna.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SannaPrimary,
    onPrimary = SannaOnPrimary,
    secondary = SannaSecondary,
    onSecondary = SannaOnSecondary,
    background = SannaBackground,
    surface = SannaSurface,
    error = SannaError
)

private val DarkColorScheme = darkColorScheme(
    primary = SannaPrimary,
    onPrimary = SannaOnPrimary,
    secondary = SannaSecondary,
    onSecondary = SannaOnSecondary,
    background = SannaBackground,
    surface = SannaSurface,
    error = SannaError
)

@Composable
fun GestorCitasSANNATheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
