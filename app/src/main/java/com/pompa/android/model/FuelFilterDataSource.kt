package com.pompa.android.model

import android.annotation.SuppressLint
import android.content.Context
import com.pompa.android.R

object FuelFilterDataSource {

    @SuppressLint("Recycle")
    fun getFilters(context: Context): List<FuelFilterItem> {

        val resources = context.resources
        val filterLabels = resources.getStringArray(R.array.fuel_filters)
        val icons = resources.obtainTypedArray(R.array.fuel_filter_icons)
        val items = filterLabels.mapIndexed { index, label ->
            FuelFilterItem(
                value = label,
                icon = icons.getResourceId(index, 0),
                selected = index == 0
            )
        }
        return items
    }
}