package com.pompa.android.model.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pompa.android.R

data class SettingsMenuUiItem(
    val option: SettingsMenuOption,
    val selectedValue: String,
)

enum class SettingsMenuOption(@param:StringRes val title: Int, @param:DrawableRes val icon: Int) {
    CITY(R.string.settings_city_option, R.drawable.ic_location),
    FAVORITE_PROVIDER(R.string.settings_favorite_provider_option, R.drawable.ic_gas_station)
}