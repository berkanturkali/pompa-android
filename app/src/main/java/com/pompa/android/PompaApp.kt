package com.pompa.android

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pompa.android.features.provinces.ui.ProvincesScreen
import com.pompa.android.navigation.PompaRoutes

@Composable
fun PompaApp(
    provinceAlreadySelected: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    Scaffold(modifier = modifier.fillMaxSize()) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = if (provinceAlreadySelected) PompaRoutes.HomeScreen else PompaRoutes.ProvincesScreen
        ) {
            composable<PompaRoutes.ProvincesScreen> {
                ProvincesScreen(
                    navigateToHomePage = {
                        navController.navigate(PompaRoutes.HomeScreen) {
                            popUpTo(PompaRoutes.ProvincesScreen) {
                                inclusive = true
                            }
                        }
                    }
                )
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