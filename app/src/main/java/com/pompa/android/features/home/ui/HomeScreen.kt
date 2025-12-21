package com.pompa.android.features.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pompa.android.features.home.components.FuelFilters
import com.pompa.android.features.home.components.FuelSearchBar
import com.pompa.android.features.home.components.PriceDateView
import com.pompa.android.features.home.viewmodel.HomeScreenViewModel
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.ui.components.FuelItem
import com.pompa.android.ui.components.PriceListBrandSection
import com.pompa.android.ui.components.SortButton
import com.pompa.android.ui.providers.pompaColorPalette

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel = hiltViewModel()) {

    HomeScreenContent(
        providers = viewModel.providers,
        modifier = modifier,
    )

}

@Composable
fun HomeScreenContent(
    providers: List<FuelPriceProvider>,
    modifier: Modifier = Modifier
) {

    val containerPadding = 8.dp

    var query by remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    val isHeaderPinned by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = containerPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(horizontal = containerPadding)
            ) {
                FuelSearchBar(
                    modifier = Modifier
                        .weight(0.8f),
                    value = query
                ) {
                    query = it
                }

                SortButton(modifier = Modifier.padding(start = 4.dp)) { }

            }


            FuelFilters(
                modifier = Modifier.padding(horizontal = containerPadding, vertical = 2.dp),
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.pompaColorPalette.borderColor
            )

            PriceDateView(
                "21/12/2025",
                modifier = Modifier.padding(end = containerPadding, top = containerPadding)
            )
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                providers.forEach { provider ->
                    stickyHeader {
                        PriceListBrandSection(
                            isHeaderPinned = isHeaderPinned,
                            name = provider.provider,
                            logo = provider.providerLogo,
                            averagePrice = provider.averagePrice.toString()
                        )
                    }

                    items(provider.data) { record ->
                        FuelItem(
                            districtName = record.districtName,
                            fuelPrices = record.prices.mapToUiItems(
                                unit = record.unit,
                                weightUnit = record.weightUnit
                            )
                                .take(4),
                            modifier = Modifier.padding(containerPadding)
                        )
                    }
                }
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun HomeScreenContentPrev() {
//    PompaTheme {
//        HomeScreenContent()
//    }
//}