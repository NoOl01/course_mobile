package com.example.matule.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF48B2E7),
    secondary = Color(0xFF2B6B8B),
    error = Color(0xFFF87265),
    tertiary = Color(0xFF6A6A6A),
    surface = Color(0xFFF7F7F9),
    background = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color(0xFFD8D8D8),
    onBackground = Color(0xFFD8D8D8),
    onSurface = Color(0xFFEFEFEF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF48B2E7),
    secondary = Color(0xFF2B6B8B),
    error = Color(0xFFF87265),
    tertiary = Color(0xFF6A6A6A),
    surface = Color(0xFFF7F7F9),
    background = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color(0xFFD8D8D8),
    onBackground = Color(0xFF2B2B2B),
    onSurface = Color(0xFF707B81)
)


@Composable
fun MatuleTheme(
    darkTheme: Boolean = false /*isSystemInDarkTheme()*/,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}