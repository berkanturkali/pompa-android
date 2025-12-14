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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pompa.android.features.brands.ui.FuelBrandsScreen
import com.pompa.android.features.provinces.ui.ProvincesScreen
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.ui.components.PompaAppBottomBar
import com.pompa.android.ui.components.PompaAppTopBar

@Composable
fun PompaApp(
    viewModel: MainActivityViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    viewModel.setIsTopBarVisible(navBackStackEntry?.destination)
    viewModel.setShowBottomBar(navBackStackEntry?.destination)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(), topBar = {
            val (code, name) = viewModel.getSelectedProvince()
            AnimatedVisibility(
                visible = viewModel.isTopBarVisible,
                enter = slideInVertically(
                    initialOffsetY = { -it }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it }
                ) + fadeOut()
            ) {
                PompaAppTopBar(
                    showBackButton = viewModel.showBackButton,
                    showSelectedProvince = viewModel.showSelectedProvince,
                    title = viewModel.topBarTitle,
                    provinceCode = code.toString(),
                    provinceName = name ?: "",
                    onBackButtonClick = {
                        navController.navigateUp()
                    }
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = viewModel.showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                ) + fadeOut()
            ) {
                PompaAppBottomBar(
                    navController = navController,
                    destinations = viewModel.topLevelDestinations
                )
            }
        }
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = /*if (viewModel.checkProvinceAlreadySelected()) PompaRoutes.HomeScreen else */ PompaRoutes.ProvincesScreen
        ) {
            composable<PompaRoutes.ProvincesScreen> {
                viewModel.topBarTitle = stringResource(R.string.select_province)
                viewModel.showBackButton = false
                ProvincesScreen(
                    navigateToFuelBrandsScreen = {
                        navController.navigate(PompaRoutes.FuelBrandsScreen)
                    }
                )
            }

            composable<PompaRoutes.FuelBrandsScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                    )
                }
            ) {
                viewModel.showBackButton = true
                viewModel.showSelectedProvince = true
                viewModel.topBarTitle = stringResource(R.string.select_fuel_brand)
                FuelBrandsScreen {
                    navController.navigate(PompaRoutes.BottomNavRoutes.Home) {
                        popUpTo(PompaRoutes.FuelBrandsScreen) {
                            inclusive = true
                        }
                    }
                }
            }

            composable<PompaRoutes.BottomNavRoutes.Home>(
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
                viewModel.topBarTitle = ""
                viewModel.showBackButton = false
            }
        }
    }
}