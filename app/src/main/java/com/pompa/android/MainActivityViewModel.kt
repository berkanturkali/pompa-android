package com.pompa.android

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var isTopBarVisible by mutableStateOf(false)

    var topBarTitle by mutableStateOf("")

    var showBackButton by mutableStateOf(false)

    var showSelectedProvince by mutableStateOf(
        !userPreferences.getSelectedProvinceCode().isNullOrBlank()
    )

    var showBottomBar by mutableStateOf(false)

    val topLevelDestinations = listOf(
        PompaRoutes.BottomNavRoutes.Home,
        PompaRoutes.BottomNavRoutes.Settings,
    )

    val topLevelDestinationsAsRoutes = topLevelDestinations.map {
        it::class.qualifiedName
    }

    fun checkProvinceAndFavoriteProviderAlreadySelected(): Boolean {
        val code = userPreferences.getSelectedProvinceCode()
        val provider = userPreferences.getFavoriteProviderName()

        return !code.isNullOrBlank() && !provider.isNullOrBlank()
    }

    fun getSelectedProvince(): Pair<String?, String?> {
        val code = userPreferences.getSelectedProvinceCode()
        val name = userPreferences.getSelectedProvinceName()
        return Pair(code, name)
    }

    fun setIsTopBarVisible(currentDestination: NavDestination?) {
        val currentRouteString = currentDestination?.route
        isTopBarVisible =
            currentRouteString != PompaRoutes.ProvincesScreen::class.qualifiedName || currentRouteString != PompaRoutes.ProvidersScreen::class.qualifiedName
    }

    fun setShowBottomBar(currentDestination: NavDestination?) {
        val currentRouteString = currentDestination?.route
        showBottomBar = currentRouteString in topLevelDestinationsAsRoutes
    }

    fun setAppTopBarTitle(title: String?) {
        topBarTitle = title
            ?.takeIf { it.isNotBlank() }
            ?: context.getString(R.string.app_name)
    }
}