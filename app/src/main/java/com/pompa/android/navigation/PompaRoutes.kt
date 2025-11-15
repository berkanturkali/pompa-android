package com.pompa.android.navigation

import kotlinx.serialization.Serializable

sealed class PompaRoutes {
    @Serializable
    object ProvincesScreen : PompaRoutes()

    @Serializable
    object HomeScreen : PompaRoutes()
}