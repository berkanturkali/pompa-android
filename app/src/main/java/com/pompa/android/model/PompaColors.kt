package com.pompa.android.model

import androidx.compose.ui.graphics.Color

data class PompaColors(
    val backgroundColors: BackgroundColors = BackgroundColors(),
    val cardColors: CardColors = CardColors(),
    val textColors: TextColors = TextColors(),
    val buttonColors: ButtonColors = ButtonColors(),
    val borderColor: Color = Color.Unspecified,
    val progressIndicatorColors: ProgressIndicatorColors = ProgressIndicatorColors(),
    val topBarColors: TopBarColors = TopBarColors(),
    val bottomBarColors: BottomBarColors = BottomBarColors(),
    val chipColors: ChipColors = ChipColors(),
    val searchBarColors: SearchBarColors = SearchBarColors()
)

data class BackgroundColors(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified
)

data class CardColors(
    val primaryBackground: Color = Color.Unspecified,
    val secondaryBackground: Color = Color.Unspecified,
)

data class TextColors(
    val primary: Color = Color.Unspecified,   // main text on card/page (black-ish)
    val secondary: Color = Color.Unspecified,   // muted on card/page (gray-ish)
    val onHighlight: Color = Color.Unspecified, // text on yellow (Opet blue)
    val onBrand: Color = Color.Unspecified,  // text on blue (white)
    val link: Color = Color.Unspecified,
)

data class ButtonColors(
    val filledPrimaryBackground: Color = Color.Unspecified,
    val filledPrimaryContent: Color = Color.Unspecified,
    val filledSecondaryBackground: Color = Color.Unspecified,
    val filledSecondaryContent: Color = Color.Unspecified,
)

data class ProgressIndicatorColors(
    val background: Color = Color.Unspecified,
    val indicator: Color = Color.Unspecified
)

data class TopBarColors(
    val background: Color = Color.Unspecified,
    val content: Color = Color.Unspecified
)

data class BottomBarColors(
    val background: Color = Color.Unspecified,
    val content: Color = Color.Unspecified,
    val selectedItemColor: Color = Color.Unspecified,
    val unSelectedItemColor: Color = Color.Unspecified,
    val indicatorColor: Color = Color.Unspecified
)

data class ChipColors(
    val unselectedBackground: Color = Color.Unspecified,
    val selectedBackground: Color = Color.Unspecified,
    val unselectedTextColor: Color = Color.Unspecified,
    val selectedTextColor: Color = Color.Unspecified,
    val selectedBorderColor: Color = Color.Unspecified,
    val unselectedBorderColor: Color = Color.Unspecified
)

data class SearchBarColors(
    val cursorColor: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val startIconColor: Color = Color.Unspecified,
    val closeIconColor: Color = Color.Unspecified,
    val hintColor: Color = Color.Unspecified,
)