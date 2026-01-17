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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.pompa.android.features.settings.ui.SettingsScreen
import com.pompa.android.features.sort.ui.SortScreen
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.navigation.args.DistrictFuelDetailsArgs
import com.pompa.android.navigation.utils.decodeNavArg
import com.pompa.android.navigation.utils.encodeNavArg
import com.pompa.android.ui.components.PompaAppBottomBar
import com.pompa.android.ui.components.PompaAppTopBar

private const val TAG = "PompaApp"

private const val BOTTOM_SHEET_NAVIGATOR_NAME = "bottomSheet"
private const val DIALOG_NAVIGATOR_NAME = "dialog"

@Composable
fun PompaApp(
    viewModel: MainActivityViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var reselectedTab by remember { mutableStateOf<PompaRoutes.BottomNavRoutes?>(null) }

    val bottomNavRoutes = listOf(
        PompaRoutes.BottomNavRoutes.Home.toString(), PompaRoutes.BottomNavRoutes.Settings.toString()
    )



    LaunchedEffect(navBackStackEntry?.destination) {
        val dest = navBackStackEntry?.destination
        val navigator = dest?.navigatorName
        val route = dest?.route?.substringAfterLast(".")

        val hasBackStack = navController.previousBackStackEntry != null
        val isBottomNavRoute = route in bottomNavRoutes
        val isOverlay =
            navigator == BOTTOM_SHEET_NAVIGATOR_NAME || navigator == DIALOG_NAVIGATOR_NAME

        viewModel.showBackButton = hasBackStack && !isBottomNavRoute && !isOverlay

        viewModel.setIsTopBarVisible(dest)
        viewModel.setShowBottomBar(dest)
        viewModel.setShowSelectedProvinceView(dest)
    }

    val bottomBarHeight = 50.dp

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(), topBar = {
            val (code, name) = viewModel.getSelectedProvince()
            AnimatedVisibility(
                visible = viewModel.isTopBarVisible, enter = slideInVertically(
                    initialOffsetY = { -it }) + fadeIn(), exit = slideOutVertically(
                    targetOffsetY = { -it }) + fadeOut()
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
                    })
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = if (viewModel.showBottomBar) bottomBarHeight
                        else 0.dp
                    )
            ) {

                NavHost(
                    navController = navController,
                    startDestination = if (viewModel.checkProvinceAndFavoriteProviderAlreadySelected()) PompaRoutes.BottomNavRoutes.Home else PompaRoutes.ProvincesScreen
                ) {
                    composable<PompaRoutes.ProvincesScreen>(enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                        )
                    }, exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                        )
                    }) {
                        viewModel.setAppTopBarTitle(stringResource(R.string.select_province))
                        ProvincesScreen(
                            navigatedFromDestination = navController.previousBackStackEntry != null,
                            navigateUp = {
                                navController.navigateUp()
                            },
                            navigateToFuelBrandsScreen = {
                                navController.navigate(PompaRoutes.ProvidersScreen)
                            },
                            selectedProvinceCode = viewModel.getSelectedProvince().first
                        )
                    }

                    composable<PompaRoutes.ProvidersScreen>(enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                        )
                    }) {
                        viewModel.setAppTopBarTitle(stringResource(R.string.select_fuel_provider))
                        ProvidersScreen {
                            if (navController.previousBackStackEntry != null) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(PompaRoutes.BottomNavRoutes.Home) {
                                    popUpTo(PompaRoutes.ProvidersScreen) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }

                    composable<PompaRoutes.BottomNavRoutes.Home>(enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                        )
                    }, exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                        )
                    }) {
                        viewModel.setAppTopBarTitle("")

                        val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

                        val tabReselected = reselectedTab == PompaRoutes.BottomNavRoutes.Home

                        HomeScreen(
                            viewModel = homeScreenViewModel,
                            onFuelItemClick = { provider, record, isFavoriteProvider ->
                                val args = encodeNavArg(
                                    DistrictFuelDetailsArgs(
                                        isFavoriteProvider = isFavoriteProvider,
                                        providerName = provider.provider,
                                        providerLogo = provider.providerLogo,
                                        fuelPrices = record.prices?.mapToUiItems(
                                            record.unit ?: "", record.weightUnit ?: ""
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
                            },
                            onReselectionConsumed = {
                                reselectedTab = null
                            },
                            tabReselected = tabReselected
                        )
                    }


                    bottomSheet(
                        route = PompaRoutes.DistrictFuelPriceDetailsScreen.route,
                    ) { entry ->
                        val argumentsAsString = entry.arguments?.getString("args")!!
                        val args: DistrictFuelDetailsArgs = decodeNavArg(argumentsAsString)
                        viewModel.setAppTopBarTitle("")

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
                    composable<PompaRoutes.BottomNavRoutes.Settings>(popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                        )
                    }, popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                        )
                    }) {
                        viewModel.setAppTopBarTitle("")
                        SettingsScreen { route ->
                            navController.navigate(route)
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                visible = viewModel.showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it }) + fadeOut()
            ) {
                PompaAppBottomBar(
                    navController = navController,
                    destinations = viewModel.topLevelDestinations,
                    onTabReselected = { destination ->
                        reselectedTab = destination
                    })
            }
        }
    }
}