package com.pompa.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompa.android.R
import com.pompa.android.features.home.model.FuelPriceUiModel
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun FuelItem(
    districtName: String,
    actualFuelPriceListCount: Int,
    fuelPrices: List<FuelPriceUiModel>,
    modifier: Modifier = Modifier,
    showDistrict: Boolean = true,
    onItemClick: () -> Unit = {},
) {

    Column {
        if (showDistrict) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .safeClickable(indication = ClickIndication.None) {
                        onItemClick()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    districtName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.pompaColorPalette.textColors.onHighlight,
                    modifier = Modifier.padding(start = 4.dp)
                )


                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = String.format(
                            stringResource(R.string.see_all),
                            actualFuelPriceListCount
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        ),
                        color = MaterialTheme.pompaColorPalette.textColors.link,
                        textDecoration = TextDecoration.Underline
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_next),
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                }
            }
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                1.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground.copy(0.95f)
            ),
            border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor)
        ) {

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(
                        MaterialTheme.pompaColorPalette.cardColors.primaryBackground.copy(0.95f)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    fuelPrices.forEachIndexed { index, priceItem ->
                        FuelPriceItem(
                            title = stringResource(priceItem.title),
                            price = priceItem.price,
                            unit = priceItem.unit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                        if (index != fuelPrices.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.pompaColorPalette.borderColor,
                                thickness = 0.5.dp
                            )
                        }

                    }
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
            actualFuelPriceListCount = 3,
            districtName = "IZMIR",
            fuelPrices = listOf(
                FuelPriceUiModel(
                    title = R.string.gasoline95,
                    price = "52, 63",
                    unit = "tl/lt"
                ),
                FuelPriceUiModel(
                    title = R.string.gasoline95,
                    price = "52, 63",
                    unit = "tl/lt"
                ),
                FuelPriceUiModel(
                    title = R.string.gasoline95,
                    price = "52, 63",
                    unit = "tl/lt"
                ),
            )
        ) {}
    }
}