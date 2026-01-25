package com.pompa.android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelPriceItem(
    title: String,
    price: String,
    unit: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.pompaColorPalette.textColors.primary,
            textAlign = TextAlign.Start,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            .toSpanStyle()
                    ) {
                        append(price)
                    }
                    withStyle(
                        MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                            .toSpanStyle()
                    ) {
                        append("₺")
                    }
                },
                color = MaterialTheme.pompaColorPalette.textColors.primary,
                textAlign = TextAlign.Center
            )

            Text(
                unit,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp
                ),
                color = MaterialTheme.pompaColorPalette.textColors.primary.copy(0.8f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun FuelPriceItemPrev() {
    PompaTheme {
        FuelPriceItem(
            title = "GASOLINE 95",
            price = "42.10",
            unit = "TL/LT"
        )
    }
}