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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.pompa.android.model.fuel.PriceSource
import com.pompa.android.model.fuel.PriceTrend
import com.pompa.android.ui.components.FuelItem
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaLoadingView
import com.pompa.android.ui.components.PriceListBrandSection
import com.pompa.android.ui.components.SortButton
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.utils.slideInByScrollDirection
import com.pompa.android.ui.utils.isTabletLayout

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
    val isTabletLayout = isTabletLayout()

    val containerPadding = 8.dp
    val headerItems = remember(providers, selectedProvider) {
        buildProviderHeaderItems(providers, selectedProvider)
    }

    var lastIndex by remember { mutableIntStateOf(0) }
    var lastOffset by remember { mutableIntStateOf(0) }

    val gridState = rememberLazyGridState()
    val listState = rememberLazyListState()

    val pullState = rememberPullToRefreshState()

    val isHeaderPinned by remember(isTabletLayout, gridState, listState) {
        derivedStateOf {
            if (isTabletLayout) {
                gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
            } else {
                listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
            }
        }
    }

    val isScrollingDown = remember(isTabletLayout, gridState, listState) {
        derivedStateOf {
            val currIndex = if (isTabletLayout) {
                gridState.firstVisibleItemIndex
            } else {
                listState.firstVisibleItemIndex
            }
            val currOffset = if (isTabletLayout) {
                gridState.firstVisibleItemScrollOffset
            } else {
                listState.firstVisibleItemScrollOffset
            }

            val scrollingDown =
                currIndex > lastIndex ||
                        (currIndex == lastIndex && currOffset > lastOffset)

            lastIndex = currIndex
            lastOffset = currOffset

            scrollingDown
        }
    }

    val pinnedHeader by remember(isTabletLayout, headerItems, gridState) {
        derivedStateOf {
            if (!isTabletLayout) {
                return@derivedStateOf null
            }

            val topVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo
                .minByOrNull { it.offset.y }
                ?.index
                ?: gridState.firstVisibleItemIndex

            headerItems.lastOrNull { it.itemIndex <= topVisibleItemIndex }
        }
    }


    LaunchedEffect(tabReselected) {
        if (tabReselected) {
            if (isTabletLayout) {
                gridState.animateScrollToItem(0)
            } else {
                listState.animateScrollToItem(0)
            }
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
                Box(modifier = Modifier.fillMaxSize()) {
                    if (isTabletLayout) {
                        TabletHomeGrid(
                            providers = providers,
                            selectedProvider = selectedProvider,
                            gridState = gridState,
                            isHeaderPinned = isHeaderPinned,
                            isScrollingDown = isScrollingDown.value,
                            containerPadding = containerPadding,
                            onFuelItemClick = onFuelItemClick,
                            onLocationButtonClick = onLocationButtonClick
                        )
                    } else {
                        PhoneHomeGrid(
                            providers = providers,
                            selectedProvider = selectedProvider,
                            listState = listState,
                            isHeaderPinned = isHeaderPinned,
                            isScrollingDown = isScrollingDown.value,
                            containerPadding = containerPadding,
                            onFuelItemClick = onFuelItemClick,
                            onLocationButtonClick = onLocationButtonClick
                        )
                    }

                    val currentPinnedHeader = pinnedHeader
                    if (isTabletLayout && isHeaderPinned && currentPinnedHeader != null) {
                        PriceListBrandSection(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isHeaderPinned = true,
                            name = currentPinnedHeader.name,
                            logo = currentPinnedHeader.logo,
                            averagePrice = currentPinnedHeader.averagePrice,
                            isFavorite = currentPinnedHeader.isFavorite,
                            showDivider = currentPinnedHeader.showDivider,
                            showInfoMessage = currentPinnedHeader.showInfoMessage
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhoneHomeGrid(
    providers: List<FuelPriceProvider>,
    selectedProvider: String,
    listState: androidx.compose.foundation.lazy.LazyListState,
    isHeaderPinned: Boolean,
    isScrollingDown: Boolean,
    containerPadding: androidx.compose.ui.unit.Dp,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean, List<PriceTrend>) -> Unit,
    onLocationButtonClick: (String, FuelPriceRecord) -> Unit,
) {
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
                    showInfoMessage = (provider.providerIsManual || provider.source == PriceSource.DATABASE) && provider.data.isNotEmpty()
                )
            }

            if (!provider.ok) {
                item {
                    ProviderErrorView(provider = provider.provider)
                }
            } else {
                val visibleRecords = provider.data.filterNotNull().mapNotNull { record ->
                    val fuelPrices = record.prices?.mapToUiItems(
                        unit = record.unit ?: "",
                        weightUnit = record.weightUnit ?: ""
                    )?.take(3) ?: emptyList()

                    if (fuelPrices.isEmpty()) null else record to fuelPrices
                }

                if (visibleRecords.isEmpty()) {
                    item {
                        ProviderEmptyView(provider = provider.provider)
                    }
                } else {
                    items(visibleRecords) { (record, fuelPrices) ->
                        val actualFuelPriceListCount = record.prices?.notNullCount() ?: 0
                        FuelItem(
                            clickable = actualFuelPriceListCount > 3,
                            districtName = record.districtName ?: "",
                            actualFuelPriceListCount = actualFuelPriceListCount,
                            fuelPrices = fuelPrices,
                            modifier = Modifier
                                .slideInByScrollDirection(isScrollingDown)
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

@Composable
private fun TabletHomeGrid(
    providers: List<FuelPriceProvider>,
    selectedProvider: String,
    gridState: LazyGridState,
    isHeaderPinned: Boolean,
    isScrollingDown: Boolean,
    containerPadding: androidx.compose.ui.unit.Dp,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean, List<PriceTrend>) -> Unit,
    onLocationButtonClick: (String, FuelPriceRecord) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        providerSections(
            providers = providers,
            selectedProvider = selectedProvider,
            isHeaderPinned = isHeaderPinned,
            isScrollingDown = isScrollingDown,
            containerPadding = containerPadding,
            fullSpan = 3,
            onFuelItemClick = onFuelItemClick,
            onLocationButtonClick = onLocationButtonClick
        )
    }
}

private fun androidx.compose.foundation.lazy.grid.LazyGridScope.providerSections(
    providers: List<FuelPriceProvider>,
    selectedProvider: String,
    isHeaderPinned: Boolean,
    isScrollingDown: Boolean,
    containerPadding: androidx.compose.ui.unit.Dp,
    fullSpan: Int,
    onFuelItemClick: (FuelPriceProvider, FuelPriceRecord, Boolean, List<PriceTrend>) -> Unit,
    onLocationButtonClick: (String, FuelPriceRecord) -> Unit,
) {
    providers.forEach { provider ->
        val isFavoriteProvider = provider.provider.equals(
            selectedProvider,
            ignoreCase = true
        )

        item(span = { GridItemSpan(fullSpan) }) {
            PriceListBrandSection(
                isHeaderPinned = isHeaderPinned,
                name = provider.provider,
                logo = provider.providerLogo,
                averagePrice = provider.averagePrice?.toString(),
                isFavorite = isFavoriteProvider,
                showDivider = provider.ok && provider.data.isNotEmpty(),
                showInfoMessage = (provider.providerIsManual || provider.source == PriceSource.DATABASE) && provider.data.isNotEmpty()
            )
        }

        if (!provider.ok) {
            item(span = { GridItemSpan(fullSpan) }) {
                ProviderErrorView(provider = provider.provider)
            }
        } else {
            val visibleRecords = provider.data.filterNotNull().mapNotNull { record ->
                val fuelPrices = record.prices?.mapToUiItems(
                    unit = record.unit ?: "",
                    weightUnit = record.weightUnit ?: ""
                )?.take(3) ?: emptyList()

                if (fuelPrices.isEmpty()) {
                    null
                } else {
                    record to fuelPrices
                }
            }

            if (visibleRecords.isEmpty()) {
                item(span = { GridItemSpan(fullSpan) }) {
                    ProviderEmptyView(provider = provider.provider)
                }
            } else {
                gridItems(visibleRecords) { (record, fuelPrices) ->
                    val actualFuelPriceListCount = record.prices?.notNullCount() ?: 0
                    FuelItem(
                        clickable = actualFuelPriceListCount > 3,
                        districtName = record.districtName ?: "",
                        actualFuelPriceListCount = actualFuelPriceListCount,
                        fuelPrices = fuelPrices,
                        modifier = Modifier
                            .slideInByScrollDirection(isScrollingDown)
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

private data class ProviderHeaderItem(
    val itemIndex: Int,
    val name: String,
    val logo: String,
    val averagePrice: String?,
    val isFavorite: Boolean,
    val showDivider: Boolean,
    val showInfoMessage: Boolean,
)

private fun buildProviderHeaderItems(
    providers: List<FuelPriceProvider>,
    selectedProvider: String,
): List<ProviderHeaderItem> {
    val headerItems = mutableListOf<ProviderHeaderItem>()
    var currentIndex = 0

    providers.forEach { provider ->
        val visibleRecords = provider.data.filterNotNull().mapNotNull { record ->
            val fuelPrices = record.prices?.mapToUiItems(
                unit = record.unit ?: "",
                weightUnit = record.weightUnit ?: ""
            )?.take(3) ?: emptyList()

            if (fuelPrices.isEmpty()) null else record to fuelPrices
        }

        headerItems += ProviderHeaderItem(
            itemIndex = currentIndex,
            name = provider.provider,
            logo = provider.providerLogo,
            averagePrice = provider.averagePrice?.toString(),
            isFavorite = provider.provider.equals(selectedProvider, ignoreCase = true),
            showDivider = provider.ok && provider.data.isNotEmpty(),
            showInfoMessage = (provider.providerIsManual || provider.source == PriceSource.DATABASE) && provider.data.isNotEmpty()
        )

        currentIndex += 1

        currentIndex += when {
            !provider.ok -> 1
            visibleRecords.isEmpty() -> 1
            else -> visibleRecords.size
        }
    }

    return headerItems
}
