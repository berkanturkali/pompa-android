package com.pompa.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pompa.android.features.district_fuel_price_details.ui.DistrictFuelPriceDetailsScreen
import com.pompa.android.features.home.ui.HomeScreen
import com.pompa.android.features.home.viewmodel.HomeScreenViewModel
import com.pompa.android.features.providers.ui.ProvidersScreen
import com.pompa.android.features.provinces.ui.ProvincesScreen
import com.pompa.android.features.sort.ui.SortScreen
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.navigation.args.DistrictFuelDetailsArgs
import com.pompa.android.navigation.utils.decodeNavArg
import com.pompa.android.navigation.utils.encodeNavArg
import com.pompa.android.ui.components.PompaAppBottomBar
import com.pompa.android.ui.components.PompaAppTopBar

private const val TAG = "PompaApp"

private const val REFRESH_LIST_KEY = "refresh_list"

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
                    },
                    onSelectedProvinceClick = {
                        navController.navigate(PompaRoutes.ProvincesScreen)
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = if (viewModel.checkProvinceAndFavoriteProviderAlreadySelected()) PompaRoutes.BottomNavRoutes.Home else PompaRoutes.ProvincesScreen
            ) {
                composable<PompaRoutes.ProvincesScreen> {
                    viewModel.setAppTopBarTitle(stringResource(R.string.select_province))
                    viewModel.showBackButton = navController.previousBackStackEntry != null
                    ProvincesScreen(
                        navigatedFromDestination = navController.previousBackStackEntry != null,
                        navigateUp = {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                REFRESH_LIST_KEY,
                                true
                            )
                            navController.navigateUp()
                        },
                        navigateToFuelBrandsScreen = {
                            navController.navigate(PompaRoutes.ProvidersScreen)
                        }
                    )
                }

                composable<PompaRoutes.ProvidersScreen>(
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
                    viewModel.setAppTopBarTitle(stringResource(R.string.select_fuel_provider))
                    ProvidersScreen {
                        navController.navigate(PompaRoutes.BottomNavRoutes.Home) {
                            popUpTo(PompaRoutes.ProvidersScreen) {
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
                    viewModel.setAppTopBarTitle("")
                    viewModel.showBackButton = false

                    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

                    LaunchedEffect(navBackStackEntry) {
                        navBackStackEntry?.savedStateHandle?.get<Boolean>(REFRESH_LIST_KEY)?.let {
                            homeScreenViewModel.fetchPrices()
                            navBackStackEntry?.savedStateHandle[REFRESH_LIST_KEY] = null
                        }
                    }

                    HomeScreen(
                        viewModel = homeScreenViewModel,
                        onFuelItemClick = { provider, record, isFavoriteProvider ->
                            val args = encodeNavArg(
                                DistrictFuelDetailsArgs(
                                    isFavoriteProvider = isFavoriteProvider,
                                    providerName = provider.provider,
                                    providerLogo = provider.providerLogo,
                                    fuelPrices = record.prices?.mapToUiItems(
                                        record.unit ?: "",
                                        record.weightUnit ?: ""
                                    ) ?: emptyList(),
                                    districtName = record.districtName ?: ""
                                )
                            )

                            navController.navigate(
                                PompaRoutes.DistrictFuelPriceDetailsScreen.createRouteWithArgs(
                                    args = args
                                )
                            )
                        },
                        onSortButtonClick = {
                            navController.navigate(PompaRoutes.SortScreen.toString())
                        })
                }


                bottomSheet(
                    route = PompaRoutes.DistrictFuelPriceDetailsScreen.route,
                ) { entry ->
                    val argumentsAsString = entry.arguments?.getString("args")!!
                    val args: DistrictFuelDetailsArgs = decodeNavArg(argumentsAsString)
                    viewModel.setAppTopBarTitle("")
                    viewModel.showBackButton = false

                    DistrictFuelPriceDetailsScreen(
                        args = args
                    )
                }


                bottomSheet(
                    route = PompaRoutes.SortScreen.toString(),
                ) {
                    SortScreen {
                        navController.navigateUp()
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
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
    }
}