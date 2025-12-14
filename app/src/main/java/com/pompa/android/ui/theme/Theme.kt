package com.pompa.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.pompa.android.model.BackgroundColors
import com.pompa.android.model.BottomBarColors
import com.pompa.android.model.ButtonColors
import com.pompa.android.model.CardColors
import com.pompa.android.model.PompaColors
import com.pompa.android.model.TextColors
import com.pompa.android.model.TopBarColors
import com.pompa.android.ui.providers.LocalPompaColors

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val OpetColors = PompaColors(
    backgroundColors = BackgroundColors(
        primary = Color(0xFF134fae),
        secondary = Color.White,
        onPrimary = Color(0xffffd300),
        onSecondary = Color(0xff134fae)
    ),
    cardColors = CardColors(
        primary = Color.White,
        secondary = Color.White,
    ),
    textColors = TextColors(
        buttonText = Color(0xff134fae),
        title = Color(0xff162133),
        description = Color(0xff162133),
        link = Color(0xFF1A73E8)
    ),
    buttonColors = ButtonColors(
        background = Color(0xffffd300),
        content = Color(0xff134fae),
        secondaryBackground = Color.White,
        secondaryContent = Color(0xff134fae)
    ),
    borderColor = Color(0xff2f3336),
    progressIndicatorColors = com.pompa.android.model.ProgressIndicatorColors(
        indicator = Color.White,
        background = Color(0xff134fae)
    ),
    topBarColors = TopBarColors(
        background = Color(0xff134fae),
        content = Color.White
    ),
    bottomBarColors = BottomBarColors(
        background = Color(0xff134fae),
        content = Color.White,
        selectedItemColor = Color.White,
        unSelectedItemColor = Color.White.copy(alpha = 0.5f),
        indicatorColor = Color(0xffffd300)
    )
)

@Composable
fun PompaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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


    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MontserratTypography,
            content = content
        )
    }
}