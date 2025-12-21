package com.pompa.android.model

import androidx.annotation.DrawableRes

data class FuelFilterItem(
    val value: String,
    @param:DrawableRes val icon: Int,
    val selected: Boolean,
)