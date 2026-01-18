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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pompa.android.features.home.components.FuelFilters
import com.pompa.android.features.home.components.FuelSearchBar
import com.pompa.android.features.home.components.PriceDateView
import com.pompa.android.features.home.components.ProviderEmptyView
import com.pompa.android.features.home.components.ProviderErrorView
import com.pompa.android.features.home.viewmodel.HomeScreenViewModel
import com.pompa.android.model.FuelFilterItem
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.fuel.FuelPriceRecord
import com.pompa.android.ui.components.FuelItem
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaLoadingView
import com.pompa.android.ui.components.PriceListBrandSection
import com.pompa.android.ui.components.SortButton
import com.pompa.android.ui.providers.pompaColorPalette

@Composable
fun HomeScreen(
    tabReselected: Boolean,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    onSortButtonClick: () -> Unit,
    onReselectionConsumed: () -> Unit,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean) -> Unit,
) {

    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        HomeScreenContent(
            providers = viewModel.providers,
            selectedProvider = viewModel.getSelectedProvider() ?: "",
            onFuelItemClick = onFuelItemClick,
            onSortButtonClick = onSortButtonClick,
            isLoading = viewModel.isLoading.value,
            onRefresh = {
                viewModel.fetchPrices(
                    cityCode = viewModel.cityCode,
                    cityName = viewModel.cityName,
                    provider = viewModel.favProviderName,
                    sortDirection = viewModel.sortDirection,
                    fuelType = viewModel.fuelType
                )
            },
            onReselectionConsumed = onReselectionConsumed,
            tabReselected = tabReselected,
            onFuelTypeSelected = { fuelFilterItem ->
                viewModel.setSelectedFuelType(fuelFilterItem.type.value)
            },
            selectedFuelFilter = viewModel.selectedFuelFilter,
            fuelFilterList = viewModel.fuelFilters,
            onLocationButtonClick = { provider, record ->
                viewModel.navigateToGoogleMapsWithLocation(
                    provider = provider,
                    districtName = record.districtName ?: "",
                    cityName = viewModel.cityName ?: ""
                )
            }
        )

        if (viewModel.isLoading.value) {
            PompaLoadingView()
        }

        PompaAppDialog(
            message = viewModel.errorMessage?.asString(context),
            onOkayButtonClick = {
                viewModel.errorMessage = null
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    isLoading: Boolean,
    fuelFilterList: List<FuelFilterItem>,
    selectedFuelFilter: FuelFilterItem?,
    tabReselected: Boolean,
    selectedProvider: String,
    providers: List<FuelPriceProvider>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onReselectionConsumed: () -> Unit,
    onSortButtonClick: () -> Unit,
    onLocationButtonClick: (String, FuelPriceRecord) -> Unit,
    onFuelTypeSelected: (FuelFilterItem) -> Unit,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean) -> Unit,
) {

    val containerPadding = 8.dp

    var query by remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    val pullState = rememberPullToRefreshState()

    val isHeaderPinned by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    LaunchedEffect(tabReselected) {
        if (tabReselected) {
            listState.animateScrollToItem(0)
            onReselectionConsumed()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(containerPadding)
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

                SortButton(modifier = Modifier.padding(start = 4.dp)) {
                    onSortButtonClick()
                }
            }

            FuelFilters(
                selectedFilter = selectedFuelFilter,
                filterList = fuelFilterList,
                modifier = Modifier.padding(horizontal = containerPadding, vertical = 2.dp),
            ) { fuelFilterItem ->
                onFuelTypeSelected(fuelFilterItem)
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.pompaColorPalette.borderColor
            )

            PriceDateView(
                "21/12/2025",
                modifier = Modifier.padding(end = containerPadding, top = containerPadding)
            )

            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize(),
                isRefreshing = isLoading,
                state = pullState,
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullState,
                        isRefreshing = isLoading,
                        containerColor = MaterialTheme.pompaColorPalette.pullToRefreshColor.container,
                        color = MaterialTheme.pompaColorPalette.pullToRefreshColor.content
                    )
                },
                onRefresh = {
                    onRefresh()
                }) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    providers.forEach { provider ->

                        val isFavoriteProvider = provider.provider.equals(
                            selectedProvider,
                            ignoreCase = true
                        )
                        stickyHeader {
                            PriceListBrandSection(
                                isHeaderPinned = isHeaderPinned,
                                name = provider.provider,
                                logo = provider.providerLogo,
                                averagePrice = provider.averagePrice?.toString(),
                                isFavorite = isFavoriteProvider,
                                showDivider = provider.ok && provider.data.isNotEmpty()
                            )
                        }
                        if (!provider.ok) {
                            item {
                                ProviderErrorView(
                                    provider = provider.provider
                                )
                            }
                        } else {
                            if (provider.data.isEmpty()) {
                                item {
                                    ProviderEmptyView(provider = provider.provider)
                                }
                            } else {
                                items(provider.data.filterNotNull()) { record ->
                                    val actualFuelPriceListCount = record.prices?.notNullCount()
                                        ?: 0
                                    FuelItem(
                                        clickable = actualFuelPriceListCount > 3,
                                        districtName = record.districtName ?: "",
                                        actualFuelPriceListCount = actualFuelPriceListCount,
                                        fuelPrices = record.prices?.mapToUiItems(
                                            unit = record.unit ?: "",
                                            weightUnit = record.weightUnit ?: ""
                                        )?.take(3) ?: emptyList(),
                                        modifier = Modifier.padding(containerPadding),
                                        onItemClick = {
                                            onFuelItemClick(provider, record, isFavoriteProvider)
                                        },
                                        onLocationButtonClick = {
                                            onLocationButtonClick(provider.provider, record)
                                        }
                                    )
                                }
                            }
                        }
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