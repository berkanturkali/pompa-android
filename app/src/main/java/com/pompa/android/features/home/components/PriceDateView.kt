package com.pompa.android.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PriceDateView(
    date: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            2.dp,
            Alignment.End
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            tint = MaterialTheme.pompaColorPalette.textColors.onHighlight.copy(0.8f),
            modifier = Modifier.size(14.dp)
        )

        Text(
            date,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.pompaColorPalette.textColors.onHighlight.copy(0.8f),
        )


    }

}

@Preview(showBackground = true)
@Composable
private fun PriceDateViewPrev() {
    PompaTheme {
        PriceDateView(
            date = "21/12/2025"
        )
    }
}