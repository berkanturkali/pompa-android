package com.pompa.android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaColor
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaAppBackButton(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    onBackButtonClick: () -> Unit = { },
) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryBackground,
            contentColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryContent
        ),
     modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            onBackButtonClick()
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            tint = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryContent,
            modifier = Modifier.size(size).padding(4.dp),
        )
    }

}

@Preview
@Composable
private fun PompaAppBackButtonPrev() {
    CompositionLocalProvider(LocalPompaColors provides PompaColor) {
        PompaTheme {
            PompaAppBackButton()
        }
    }
}