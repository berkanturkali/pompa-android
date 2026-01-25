package com.pompa.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun SortButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    Card(
        modifier = modifier
            .size(28.dp)
            .safeClickable(indication = ClickIndication.Ripple) {
                onClick()
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground,
            contentColor = MaterialTheme.pompaColorPalette.textColors.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_swap),
                contentDescription = null,
                modifier = Modifier.padding(6.dp)
            )
        }
    }

}

@Preview
@Composable
private fun SortButtonPrev() {
    PompaTheme {
        SortButton() { }
    }
}