package com.pompa.android.features.sort.model

data class SortOption(
    val title: String,
    val selected: Boolean = false,
    var direction: SortDirection = SortDirection.ASCENDING,
)

enum class SortDirection(val value: Int) {
    ASCENDING(0),
    DESCENDING(1)
}
