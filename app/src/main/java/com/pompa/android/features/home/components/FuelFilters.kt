package com.pompa.android.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.pompa.android.model.FuelFilterDataSource
import com.pompa.android.model.FuelType
import com.pompa.android.ui.components.FuelFilterChip
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelFilters(
    modifier: Modifier = Modifier,
    onFilterSelected: (FuelType) -> Unit
) {

    val context = LocalContext.current

    val filterList = FuelFilterDataSource.getFilters(context = context)

    var selectedFilter by remember {
        mutableStateOf(
            filterList.first()
        )
    }

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
                        selectedFilter = filterItem
                        onFilterSelected(filterItem.type)
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
        FuelFilters {}
    }
}
