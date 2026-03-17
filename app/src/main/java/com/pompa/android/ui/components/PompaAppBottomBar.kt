package com.pompa.android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PompaAppBottomBar(
    destinations: List<PompaRoutes.BottomNavRoutes>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onTabReselected: (PompaRoutes.BottomNavRoutes) -> Unit
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Column(modifier = modifier) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.pompaColorPalette.borderColor,
            modifier = Modifier.fillMaxWidth()
        )
        Surface(
            modifier = modifier,
            shadowElevation = 12.dp,
        ) {
            NavigationBar(
                modifier = modifier.height(46.dp),
                containerColor = MaterialTheme.pompaColorPalette.bottomBarColors.background,
                contentColor = MaterialTheme.pompaColorPalette.bottomBarColors.content
            ) {
                destinations.forEach { destination ->
                    val selected = currentRoute == destination::class.qualifiedName

                    NavigationBarItem(
                        selected = selected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.pompaColorPalette.bottomBarColors.selectedItemColor,
                            unselectedIconColor = MaterialTheme.pompaColorPalette.bottomBarColors.unSelectedItemColor,
                            indicatorColor = MaterialTheme.pompaColorPalette.bottomBarColors.indicatorColor
                        ),
                        onClick = {
                            if (selected) {
                                if (destination.scrollable) {
                                    onTabReselected(destination)
                                }
                            } else {
                                navController.navigate(destination) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.animateContentSize()
                            ) {
                                Icon(
                                    painter = painterResource(destination.icon),
                                    contentDescription = stringResource(destination.title),
                                )
                                AnimatedVisibility(
                                    visible = selected,
                                    enter = scaleIn(),
                                    exit = scaleOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(
                                                MaterialTheme.pompaColorPalette.bottomBarColors.selectedItemColor,
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        },
                        alwaysShowLabel = false
                    )

                }
            }
        }
    }

}

@Composable
fun PompaAppNavigationRail(
    destinations: List<PompaRoutes.BottomNavRoutes>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onTabReselected: (PompaRoutes.BottomNavRoutes) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(108.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.pompaColorPalette.bottomBarColors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pompa",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.pompaColorPalette.bottomBarColors.selectedItemColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.pompaColorPalette.borderColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            NavigationRail(
                containerColor = MaterialTheme.pompaColorPalette.bottomBarColors.background,
                contentColor = MaterialTheme.pompaColorPalette.bottomBarColors.content
            ) {
                for (destination in destinations) {
                    val selected = currentRoute == destination::class.qualifiedName

                    NavigationRailItem(
                        selected = selected,
                        onClick = {
                            if (selected) {
                                if (destination.scrollable) {
                                    onTabReselected(destination)
                                }
                            } else {
                                navController.navigate(destination) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = MaterialTheme.pompaColorPalette.bottomBarColors.selectedItemColor,
                            unselectedIconColor = MaterialTheme.pompaColorPalette.bottomBarColors.unSelectedItemColor,
                            indicatorColor = MaterialTheme.pompaColorPalette.bottomBarColors.indicatorColor
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = stringResource(destination.title),
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(destination.title),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PompaAppBottomBarPrev() {
    PompaTheme {
        PompaAppBottomBar(
            navController = rememberNavController(),
            destinations = listOf(
                PompaRoutes.BottomNavRoutes.Home,
                PompaRoutes.BottomNavRoutes.Settings
            )
        ) {

        }
    }
}
