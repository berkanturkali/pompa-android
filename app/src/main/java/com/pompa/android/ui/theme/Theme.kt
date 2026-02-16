package com.pompa.android.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.pompa.android.model.BackgroundColors
import com.pompa.android.model.BottomBarColors
import com.pompa.android.model.ButtonColors
import com.pompa.android.model.CardColors
import com.pompa.android.model.ChipColors
import com.pompa.android.model.PompaColors
import com.pompa.android.model.PullToRefreshColors
import com.pompa.android.model.SearchBarColors
import com.pompa.android.model.TextColors
import com.pompa.android.model.TopBarColors
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette

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

val PompaColor = PompaColors(
    backgroundColors = BackgroundColors(
        primary = Color(0xff0B0E14),
        secondary = Color.White,
    ),
    cardColors = CardColors(
        primaryBackground = Color(0xff161B22),
        secondaryBackground = Color(0xffF5C518),
    ),
    textColors = TextColors(
        primary = Color(0xffFFFFFF),
        secondary = Color(0xff8B949E),
        link = Color(0xFF1A73E8),
    ),
    buttonColors = ButtonColors(
        filledPrimaryBackground = Color(0xff2B59FF),
        filledPrimaryContent = Color.White,
    ),
    borderColor = Color(0xff30363D),
    topBarColors = TopBarColors(
        background = Color(0xff0B0E14),
        content = Color.White
    ),
    bottomBarColors = BottomBarColors(
        background = Color(0xff0B0E14),
        content = Color.White,
        selectedItemColor = Color(0xffF5C518),
        unSelectedItemColor = Color.White.copy(alpha = 0.5f),
        indicatorColor = Color.White.copy(alpha = 0.2f)
    ),
    chipColors = ChipColors(
        unselectedBackground = Color(0xff161B22),
        selectedBackground = Color(0xff0B0E14),
        unselectedTextColor = Color(0xff8B949E),
        selectedTextColor = Color(0xffF5C518),
        selectedBorderColor = Color(0xffF5C518),
        unselectedBorderColor = Color(0xff30363D),
    ),
    searchBarColors = SearchBarColors(
        backgroundColor = Color(0xff161B22),
        cursorColor = Color.White,
        textColor = Color.White,
        startIconColor = Color(0xff8B949E),
        closeIconColor = Color.White,
        hintColor = Color(0xff8B949E),
    ),
    pullToRefreshColor = PullToRefreshColors(
        container = Color(0xff0B0E14),
        content = Color.White
    ),
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


    CompositionLocalProvider(LocalPompaColors provides PompaColor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MontserratTypography,
            content = content
        )
    }
}

@Composable
fun ChangeSystemBarsTheme() {
    val activity = LocalActivity.current as ComponentActivity
    val barColor = MaterialTheme.pompaColorPalette.backgroundColors.primary.toArgb()
    LaunchedEffect(barColor) {
        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(barColor),
            navigationBarStyle = SystemBarStyle.dark(barColor),
        )
    }
}