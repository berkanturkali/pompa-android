package com.pompa.android.features.home.model

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
data class FuelPriceUiModel(
    @StringRes val title: Int,
    val price: String,
    val unit: String,
)