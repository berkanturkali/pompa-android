package com.pompa.android.features.home.model

import androidx.annotation.StringRes

data class FuelPriceUiModel(
    @StringRes val title: Int,
    val price: String,
    val unit: String,
)