package com.pompa.android.features.sort

import android.content.Context
import com.pompa.android.R
import com.pompa.android.features.sort.model.SortDirection
import com.pompa.android.features.sort.model.SortOption

object SortDataSource {

    fun getSortOptions(context: Context): List<SortOption> {
        return context.resources.getStringArray(R.array.sort_options).toList()
            .mapIndexed { index, option ->
                SortOption(
                    title = option,
                    selected = index == 0,
                    direction = if (index == 0) SortDirection.ASCENDING else SortDirection.DESCENDING
                )
            }
    }
}