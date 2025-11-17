package com.pompa.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pompa.android.features.brands.ui.FuelBrandsScreen
import com.pompa.android.features.provinces.ui.ProvincesScreen
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.ui.components.PompaAppTopBar

@Composable
fun PompaApp(
    viewModel: MainActivityViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    viewModel.setIsTopBarVisible(navBackStackEntry?.destination)

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        val (code, name) = viewModel.getSelectedProvince()
        AnimatedVisibility(
            visible = viewModel.isTopBarVisible,
            enter = slideInVertically(
                initialOffsetY = { -it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it }
            ) + fadeOut()
        ) { }
        PompaAppTopBar(
            provinceCode = code.toString(),
            provinceName = name ?: ""
        )
    }) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = if (viewModel.checkProvinceAlreadySelected()) PompaRoutes.HomeScreen else PompaRoutes.ProvincesScreen
        ) {
            composable<PompaRoutes.ProvincesScreen> {
                ProvincesScreen(
                    navigateToFuelBrandsScreen = {
                        navController.navigate(PompaRoutes.FuelBrandsScreen)
                    }
                )
            }

            composable<PompaRoutes.FuelBrandsScreen> {
                FuelBrandsScreen {
                    navController.navigate(PompaRoutes.HomeScreen) {
                        popUpTo(PompaRoutes.FuelBrandsScreen) {
                            inclusive = true
                        }
                    }
                }
            }

            composable<PompaRoutes.HomeScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                    )
                }
            ) {

            }
        }
    }
}