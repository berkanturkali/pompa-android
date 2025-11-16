package com.pompa.android.model

import androidx.compose.ui.graphics.Color

data class PompaColors(
    val backgroundColors: BackgroundColors = BackgroundColors(),
    val cardColors: CardColors = CardColors(),
    val textColors: TextColors = TextColors(),
    val buttonColors: ButtonColors = ButtonColors(),
    val borderColor: Color = Color.Unspecified,
    val progressIndicatorColors: ProgressIndicatorColors = ProgressIndicatorColors(),
    val topBarColors: TopBarColors = TopBarColors()
)

data class BackgroundColors(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val onPrimary: Color = Color.Unspecified,
    val onSecondary: Color = Color.Unspecified
)

data class CardColors(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified
)

data class TextColors(
    val buttonText: Color = Color.Unspecified,
    val title: Color = Color.Unspecified,
    val description: Color = Color.Unspecified,
)

data class ButtonColors(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
)

data class ProgressIndicatorColors(
    val background: Color = Color.Unspecified,
    val indicator: Color = Color.Unspecified
)

data class TopBarColors(
    val background: Color = Color.Unspecified,
    val content: Color = Color.Unspecified
)