package com.pompa.android.ui.providers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.pompa.android.model.PompaColors

val MaterialTheme.pompaColorPalette: PompaColors
    @Composable
    @ReadOnlyComposable
    get() = LocalPompaColors.current

val LocalPompaColors = staticCompositionLocalOf { PompaColors() }