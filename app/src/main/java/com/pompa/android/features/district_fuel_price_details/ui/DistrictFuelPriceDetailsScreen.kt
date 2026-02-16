package com.pompa.android.features.district_fuel_price_details.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.features.district_fuel_price_details.components.ProviderView
import com.pompa.android.features.home.model.FuelPriceUiModel
import com.pompa.android.model.fuel.PriceTrend
import com.pompa.android.navigation.args.DistrictFuelDetailsArgs
import com.pompa.android.ui.components.FuelItem
import com.pompa.android.ui.components.PompaBottomSheetHandle
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun DistrictFuelPriceDetailsScreen(
    args: DistrictFuelDetailsArgs,
    modifier: Modifier = Modifier
) {
    DistrictFuelPriceDetailsScreenContent(
        modifier = modifier,
        providerName = args.providerName,
        providerLogo = args.providerLogo,
        isProviderFavorite = args.isFavoriteProvider,
        fuelPrices = args.fuelPrices,
        districtName = args.districtName,
        priceTrends = args.priceTrends
    )
}

@Composable
fun DistrictFuelPriceDetailsScreenContent(
    providerName: String,
    providerLogo: String,
    isProviderFavorite: Boolean,
    districtName: String,
    fuelPrices: List<FuelPriceUiModel>,
    priceTrends: List<PriceTrend>,
    modifier: Modifier = Modifier,
) {

    val sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(sheetShape)
            .border(
                BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor),
                shape = sheetShape
            )
            .heightIn(320.dp)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        PompaBottomSheetHandle()

        Text(
            districtName,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Black),
            color = MaterialTheme.pompaColorPalette.textColors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.pompaColorPalette.borderColor
        )
        ProviderView(
            name = providerName,
            logo = providerLogo,
            isFavorite = isProviderFavorite,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        FuelItem(
            districtName = districtName,
            fuelPrices = fuelPrices,
            showDistrict = false,
            actualFuelPriceListCount = 0,
            clickable = false,
            fuelPriceTrends = priceTrends
        )

    }
}

@Preview
@Composable
private fun DistrictFuelPriceDetailsScreenContentPrev() {
    PompaTheme {
        DistrictFuelPriceDetailsScreenContent(
            providerName = "Opet",
            providerLogo = "",
            isProviderFavorite = true,
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
            ),
            listOf(
                PriceTrend(
                    fuelKey = "gasoline95",
                    previousPrice = 56.57,
                    priceChange = 1.62,
                    changeDirection = "UP"
                ),
                PriceTrend(
                    fuelKey = "gasoline95",
                    previousPrice = 56.57,
                    priceChange = 1.62,
                    changeDirection = "UP"
                ),
                PriceTrend(
                    fuelKey = "gasoline95",
                    previousPrice = 56.57,
                    priceChange = 1.62,
                    changeDirection = "UP"
                ),
            ),
        )
    }
}