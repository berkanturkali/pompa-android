package com.pompa.android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pompa.android.R
import com.pompa.android.navigation.args.DistrictFuelDetailsArgs
import kotlinx.serialization.Serializable

@Serializable
sealed class PompaRoutes {
    @Serializable
    object ProvincesScreen : PompaRoutes()

    @Serializable
    object FuelBrandsScreen : PompaRoutes()

    @Serializable
    object DistrictFuelPriceDetailsScreen : PompaRoutes() {
        val route = "$this?args={args}"

        fun createRouteWithArgs(args: String): String {
            return "${this}?args=$args"
        }
    }

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