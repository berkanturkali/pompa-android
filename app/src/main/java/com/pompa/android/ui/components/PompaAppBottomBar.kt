package com.pompa.android.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun PompaAppBottomBar(
    destinations: List<PompaRoutes.BottomNavRoutes>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onTabReselected: (PompaRoutes.BottomNavRoutes) -> Unit
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
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
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(destination.title),
                        )
                    },
                    alwaysShowLabel = false
                )

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