package com.pompa.android.features.district_fuel_price_details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.features.district_fuel_price_details.components.ProviderView
import com.pompa.android.features.home.model.FuelPriceUiModel
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.fuel.FuelPriceRecord
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
        districtName = args.districtName
    )

}

@Composable
fun DistrictFuelPriceDetailsScreenContent(
    providerName: String,
    providerLogo: String,
    isProviderFavorite: Boolean,
    districtName: String,
    fuelPrices: List<FuelPriceUiModel>,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(320.dp)
            .padding(horizontal = 8.dp)
    ) {
        PompaBottomSheetHandle()

        ProviderView(
            name = providerName,
            logo = providerLogo,
            isFavorite = isProviderFavorite,
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.pompaColorPalette.borderColor
        )

        FuelItem(
            districtName = districtName,
            fuelPrices = fuelPrices,
            showArrow = false,
            actualFuelPriceListCount = 0
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
            )
        )
    }
}