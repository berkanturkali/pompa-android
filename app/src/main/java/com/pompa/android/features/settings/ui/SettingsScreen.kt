package com.pompa.android.features.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pompa.android.BuildConfig
import com.pompa.android.R
import com.pompa.android.features.settings.components.SettingsItem
import com.pompa.android.features.settings.viewmodel.SettingsScreenViewModel
import com.pompa.android.model.settings.SettingsMenuOption
import com.pompa.android.model.settings.SettingsMenuUiItem
import com.pompa.android.navigation.PompaRoutes
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    navigateToDestination: (route: PompaRoutes) -> Unit,
) {

    val menu by viewModel.menu.collectAsState()

    SettingsScreenContent(
        menu = menu,
        modifier = modifier,
        navigateToDestination = navigateToDestination
    )
}

@Composable
fun SettingsScreenContent(
    menu: List<SettingsMenuUiItem>,
    modifier: Modifier = Modifier,
    navigateToDestination: (route: PompaRoutes) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerticalDivider(
                        thickness = 5.dp,
                        color = MaterialTheme.pompaColorPalette.cardColors.secondaryBackground,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Text(
                        stringResource(R.string.preferences),
                        color = MaterialTheme.pompaColorPalette.textColors.onHighlight,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            item {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground.copy(
                            0.95f
                        )
                    ),
                    border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor)
                ) {

                    menu.forEachIndexed { index, menuItem ->
                        SettingsItem(
                            title = stringResource(menuItem.option.title),
                            value = menuItem.selectedValue,
                            icon = menuItem.option.icon
                        ) {
                            when (menuItem.option) {
                                SettingsMenuOption.CITY -> {
                                    navigateToDestination(PompaRoutes.ProvincesScreen)
                                }

                                SettingsMenuOption.FAVORITE_PROVIDER -> {
                                    navigateToDestination(PompaRoutes.ProvidersScreen)
                                }
                            }
                        }

                        if (index != menu.lastIndex) {
                            HorizontalDivider(
                                thickness = 0.8.dp,
                                color = MaterialTheme.pompaColorPalette.borderColor,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }


            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 20.dp),
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.pompaColorPalette.borderColor,
                    )

                    Text(
                        text = stringResource(R.string.pompa_version, BuildConfig.VERSION_NAME),
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.pompaColorPalette.borderColor,
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPrev() {
    PompaTheme {
        SettingsScreenContent(
            SettingsMenuOption.entries.map {
                SettingsMenuUiItem(
                    option = it,
                    selectedValue = ""
                )
            }
        ) { }
    }
}