package com.pompa.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.ChangeSystemBarsTheme
import com.pompa.android.ui.theme.PompaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PompaTheme {
                ChangeSystemBarsTheme()
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    sheetBackgroundColor = MaterialTheme.pompaColorPalette.backgroundColors.primary,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                ) {
                    PompaApp(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}