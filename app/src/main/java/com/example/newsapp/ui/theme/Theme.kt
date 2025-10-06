// In file: app/src/main/java/com/example/newsapp/ui/theme/Theme.kt

package com.example.newsapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Defining our custom Light Color Scheme for the "Modern Newsprint" theme
private val LightNewsprintColorScheme = lightColorScheme(
    primary = DeepRed,          // Accent color for buttons, active indicators
    onPrimary = Color.White,    // Text color on top of primary elements
    background = OffWhite,      // App background
    surface = OffWhite,         // Color of cards, sheets
    onBackground = DarkText,    // Main text color
    onSurface = DarkText        // Main text color for components
)

private val DarkNewsprintColorScheme = darkColorScheme(
    primary = DeepRed,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onBackground = OffWhite,
    onSurface = OffWhite
)

@Composable
fun NewsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkNewsprintColorScheme else LightNewsprintColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NewsprintTypography,
        content = content
    )
}