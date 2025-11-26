package com.pompa.android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pompa.android.R
import kotlinx.serialization.Serializable

@Serializable
sealed class PompaRoutes {
    @Serializable
    object ProvincesScreen : PompaRoutes()

    @Serializable
    object FuelBrandsScreen : PompaRoutes()

    @Serializable
    sealed class BottomNavRoutes(
        @param:DrawableRes val icon: Int,
        @param:StringRes val title: Int
    ) : PompaRoutes() {
        @Serializable
        data object Home : BottomNavRoutes(
            icon = R.drawable.ic_home,
            title = R.string.home,
        )

        @Serializable
        data object Settings : BottomNavRoutes(
            icon = R.drawable.ic_settings,
            title = R.string.settings
        )
    }
}