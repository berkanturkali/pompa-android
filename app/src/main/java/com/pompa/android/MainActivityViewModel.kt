package com.pompa.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    var isTopBarVisible by mutableStateOf(false)

    fun checkProvinceAlreadySelected(): Boolean {
        val code = userPreferences.getSelectedProvinceCode()
        return code != -1
    }

    fun getSelectedProvince(): Pair<Int, String?> {
        val code = userPreferences.getSelectedProvinceCode()
        val name = userPreferences.getSelectedProvinceName()
        return Pair(code, name)
    }

    fun setIsTopBarVisible(currentDestination: NavDestination?) {
        val currentRouteString = currentDestination?.route
        isTopBarVisible = currentRouteString != PompaRoutes.ProvincesScreen::class.qualifiedName
    }
}