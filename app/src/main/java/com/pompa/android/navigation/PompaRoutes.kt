package com.pompa.android.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class PompaRoutes {
    @Serializable
    object ProvincesScreen : PompaRoutes()

    @Serializable
    object HomeScreen : PompaRoutes()

    @Serializable
    object FuelBrandsScreen : PompaRoutes()
}