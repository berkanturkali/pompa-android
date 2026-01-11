package com.pompa.android.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.pompa.android.model.FuelFilterDataSource
import com.pompa.android.model.FuelFilterItem
import com.pompa.android.ui.components.FuelFilterChip
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelFilters(
    filterList: List<FuelFilterItem>,
    selectedFilter: FuelFilterItem?,
    modifier: Modifier = Modifier,
    onFilterSelected: (FuelFilterItem) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filterList.forEach { filterItem ->
            FuelFilterChip(
                selected = selectedFilter == filterItem,
                filter = filterItem.value,
                icon = filterItem.icon,
                onItemClick = {
                    if (selectedFilter != filterItem) {
                        onFilterSelected(filterItem)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelFiltersPrev() {
    PompaTheme {
        FuelFilters(
            filterList = FuelFilterDataSource.getFilters(context = LocalContext.current),
            selectedFilter = FuelFilterDataSource.getFilters(context = LocalContext.current).first()
        ) {}
    }
}
