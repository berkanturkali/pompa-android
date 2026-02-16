package com.pompa.android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompa.android.R
import com.pompa.android.features.home.model.PriceTrendUiModel
import com.pompa.android.model.fuel.ChangeDirection
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelPriceItem(
    title: String,
    price: String,
    unit: String,
    priceTrend: PriceTrendUiModel,
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
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                priceTrend.icon?.let { id ->
                    Icon(
                        painter = painterResource(id),
                        contentDescription = null,
                        tint = priceTrend.color ?: Color.Transparent,
                        modifier = Modifier.size(16.dp)
                    )
                }

                priceTrend.priceChange?.let { change ->
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                            ) {
                                val sign = when (priceTrend.changeDirection) {
                                    ChangeDirection.UP -> "+"
                                    ChangeDirection.DOWN -> "-"
                                    else -> ""
                                }
                                append(sign)
                            }
                            withStyle(
                                MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                            ) {
                                append(change)
                            }
                            withStyle(
                                MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                    .toSpanStyle()
                            ) {
                                append("₺")
                            }
                        },
                        color = priceTrend.color ?: Color.Transparent,
                        textAlign = TextAlign.Center
                    )
                }
            }
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
            unit = "TL/LT",
            priceTrend = PriceTrendUiModel(
                fuelKey = "gasoline95",
                previousPrice = "56.57",
                priceChange = "1.62",
                changeDirection = ChangeDirection.UP,
                color = Color.Green,
                icon = R.drawable.ic_upward
            )
        )
    }
}