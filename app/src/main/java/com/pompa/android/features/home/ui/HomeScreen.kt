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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.pompa.android.model.fuel.PriceTrend
import com.pompa.android.ui.components.FuelItem
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaLoadingView
import com.pompa.android.ui.components.PriceListBrandSection
import com.pompa.android.ui.components.SortButton
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.utils.slideInByScrollDirection

@Composable
fun HomeScreen(
    tabReselected: Boolean,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    onSortButtonClick: () -> Unit,
    onReselectionConsumed: () -> Unit,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean, List<PriceTrend>) -> Unit,
) {

    val context = LocalContext.current

    val rawQuery by viewModel.searchQuery.collectAsState()

    val debouncedQuery by viewModel.debouncedSearchQuery
        .collectAsState(initial = "")

    val filtered = remember(debouncedQuery, viewModel.results) {
        viewModel.getFilteredResults(debouncedQuery)
    }


    Box(modifier = modifier.fillMaxSize()) {
        HomeScreenContent(
            providers = filtered,
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
                    fuelType = viewModel.fuelType,
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
            },
            date = viewModel.date,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)
            },
            searchQuery = rawQuery
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
    searchQuery: String,
    date: String,
    fuelFilterList: List<FuelFilterItem>,
    selectedFuelFilter: FuelFilterItem?,
    tabReselected: Boolean,
    selectedProvider: String,
    providers: List<FuelPriceProvider>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onReselectionConsumed: () -> Unit,
    onSortButtonClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onLocationButtonClick: (String, FuelPriceRecord) -> Unit,
    onFuelTypeSelected: (FuelFilterItem) -> Unit,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean, List<PriceTrend>) -> Unit,
) {

    val containerPadding = 8.dp

    var lastIndex by remember { mutableIntStateOf(0) }
    var lastOffset by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState()

    val pullState = rememberPullToRefreshState()

    val isHeaderPinned by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    val isScrollingDown = remember {
        derivedStateOf {
            val currIndex = listState.firstVisibleItemIndex
            val currOffset = listState.firstVisibleItemScrollOffset

            val scrollingDown =
                currIndex > lastIndex ||
                        (currIndex == lastIndex && currOffset > lastOffset)

            lastIndex = currIndex
            lastOffset = currOffset

            scrollingDown
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
                    value = searchQuery
                ) {
                    onQueryChange(it)
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
                date = date,
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
                                showDivider = provider.ok && provider.data.isNotEmpty(),
                                isManual = provider.providerIsManual
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
                                        modifier = Modifier
                                            .slideInByScrollDirection(isScrollingDown.value)
                                            .padding(containerPadding),
                                        onItemClick = {
                                            onFuelItemClick(
                                                provider,
                                                record,
                                                isFavoriteProvider,
                                                record.priceTrends ?: emptyList()
                                            )
                                        },
                                        onLocationButtonClick = {
                                            onLocationButtonClick(provider.provider, record)
                                        },
                                        fuelPriceTrends = record.priceTrends ?: emptyList()

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