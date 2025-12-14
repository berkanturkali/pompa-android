package com.pompa.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelItem(
    province: String,
    fuelPrices: List<String>,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.5.dp, color = MaterialTheme.pompaColorPalette.borderColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.cardColors.primary
        )
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                province,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.pompaColorPalette.textColors.title,
                modifier = Modifier.padding(start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fuelPrices.forEach { price ->
                    FuelPriceItem(
                        title = "GASOLINE 95",
                        price = price,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun FuelItemPrev() {
    PompaTheme {
        FuelItem(
            province = "IZMIR",
            fuelPrices = listOf(
                "41.85",
                "44.15",
                "21.85"
            )
        )
    }
}