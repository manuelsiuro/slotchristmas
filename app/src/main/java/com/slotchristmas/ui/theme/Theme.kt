package com.slotchristmas.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ChristmasColorScheme = darkColorScheme(
    primary = ChristmasRed,
    onPrimary = ChristmasWhite,
    primaryContainer = ChristmasDarkRed,
    onPrimaryContainer = ChristmasWhite,
    secondary = ChristmasGreen,
    onSecondary = ChristmasWhite,
    secondaryContainer = ChristmasDarkGreen,
    onSecondaryContainer = ChristmasWhite,
    tertiary = ChristmasGold,
    onTertiary = ChristmasDarkGreen,
    background = ChristmasDarkGreen,
    onBackground = ChristmasWhite,
    surface = ChristmasDarkGreen,
    onSurface = ChristmasWhite,
    surfaceVariant = ReelBackground,
    onSurfaceVariant = TextSecondary,
    error = ChristmasRed,
    onError = ChristmasWhite
)

@Composable
fun SlotChristmasTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = ChristmasColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ChristmasDarkGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
