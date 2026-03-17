package com.pompa.android.features.provinces.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pompa.android.R
import com.pompa.android.features.provinces.viewmodel.ProvincesScreenViewModel
import com.pompa.android.model.provinces.Province
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaColor
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.ui.utils.isTabletLayout
import com.pompa.android.ui.utils.slideInByScrollDirection
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
fun ProvincesScreen(
    navigatedFromDestination: Boolean,
    navigateUp: () -> Unit,
    navigateToFuelBrandsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    selectedProvinceCode: String? = null,
    viewModel: ProvincesScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    ProvincesScreenContent(
        provinces = viewModel.provinceList,
        modifier = modifier,
        errorMessage = viewModel.errorMessage?.asString(context),
        onProvinceSelected = { province ->
            viewModel.saveSelectedProvince(
                province = province
            )
            if (navigatedFromDestination) {
                navigateUp()
            } else {
                navigateToFuelBrandsScreen()
            }
        },
        onErrorDialogDismiss = {
            viewModel.errorMessage = null
        },
        selectedProvinceCode = selectedProvinceCode,
        showLoading = viewModel.showLoading.value
    )

}

@Composable
fun ProvincesScreenContent(
    showLoading: Boolean,
    provinces: List<Province>,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    selectedProvinceCode: String? = null,
    onErrorDialogDismiss: () -> Unit = {},
    onProvinceSelected: (Province) -> Unit
) {
    val isTabletLayout = isTabletLayout()
    val gridColumnCount = provinceGridColumnCount()

    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    var lastIndex by remember { mutableIntStateOf(0) }
    var lastOffset by remember { mutableIntStateOf(0) }

    val animatedProgress = remember { Animatable(initialValue = 300f) }
    val opacityProgress = remember { Animatable(initialValue = 0f) }

    val isScrollingDown = remember {
        derivedStateOf {
            val currIndex = if (isTabletLayout) {
                gridState.firstVisibleItemIndex
            } else {
                listState.firstVisibleItemIndex
            }
            val currOffset = if (isTabletLayout) {
                gridState.firstVisibleItemScrollOffset
            } else {
                listState.firstVisibleItemScrollOffset
            }

            val scrollingDown =
                currIndex > lastIndex ||
                        (currIndex == lastIndex && currOffset > lastOffset)

            lastIndex = currIndex
            lastOffset = currOffset

            scrollingDown
        }
    }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 0f,
            animationSpec = tween(300, easing = LinearEasing)
        )
        opacityProgress.animateTo(targetValue = 1f, animationSpec = tween(600))
    }
    val animationModifier = Modifier
        .padding(8.dp)
        .graphicsLayer(translationY = animatedProgress.value)
        .alpha(opacityProgress.value)

    Box(modifier = modifier.fillMaxSize()) {
        if (isTabletLayout) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumnCount),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                gridItems(provinces) { province ->
                    ProvinceGridItem(
                        scrollingDown = isScrollingDown.value,
                        modifier = animationModifier,
                        province = province,
                        showSelectedCheckMark = province.code == selectedProvinceCode
                    ) {
                        onProvinceSelected(province)
                    }
                }
            }
        } else {
            LazyColumn(
                state = listState
            ) {
                items(provinces) { province ->
                    ProvinceItem(
                        scrollingDown = isScrollingDown.value,
                        modifier = animationModifier,
                        province = province,
                        showSelectedCheckMark = province.code == selectedProvinceCode
                    ) {
                        onProvinceSelected(province)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showLoading,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.pompaColorPalette.backgroundColors.primary,
            )
        }

        AnimatedVisibility(
            visible = errorMessage != null,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            errorMessage?.let {
                PompaAppDialog(message = it) {
                    onErrorDialogDismiss()
                }
            }

        }
    }
}

@Composable
private fun provinceGridColumnCount(): Int {
    val density = LocalDensity.current
    val widthDp = with(density) { LocalWindowInfo.current.containerSize.width.toDp() }
    return if (widthDp >= 1200.dp) 4 else 3
}

@Composable
fun ProvinceItem(
    scrollingDown: Boolean,
    province: Province,
    modifier: Modifier = Modifier,
    showSelectedCheckMark: Boolean = false,
    onItemClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .slideInByScrollDirection(scrollingDown = scrollingDown)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onItemClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground
        ),
        border = BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground,
                        contentColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryContent
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = province.code,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Text(
                    text = province.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.pompaColorPalette.textColors.primary
                )
            }

            AnimatedVisibility(
                visible = showSelectedCheckMark,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(
                        R.drawable.ic_check
                    ),
                    contentDescription = null,
                    tint = Color(0xff4CAF50)
                )
            }
        }
    }
}

@Composable
fun ProvinceGridItem(
    scrollingDown: Boolean,
    province: Province,
    modifier: Modifier = Modifier,
    showSelectedCheckMark: Boolean = false,
    onItemClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .slideInByScrollDirection(scrollingDown = scrollingDown)
            .aspectRatio(1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onItemClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground
        ),
        border = BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                if (showSelectedCheckMark) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color(0xff4CAF50)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground,
                        contentColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryContent
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.pompaColorPalette.borderColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = province.code,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Text(
                    text = province.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.pompaColorPalette.textColors.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProvinceGridItemPrev() {
    PompaTheme {
        ProvinceGridItem(
            scrollingDown = true,
            province = Province(id = 1, name = "İstanbul", code = "34"),
            showSelectedCheckMark = true,
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun ProvinceItemPrev() {
    CompositionLocalProvider(LocalPompaColors provides PompaColor) {
        PompaTheme {
            Column {
                ProvinceItem(
                    scrollingDown = true,
                    province = Province(id = 1, name = "İstanbul", code = "34")
                ) {}

                ProvinceItem(
                    scrollingDown = true,
                    province = Province(id = 1, name = "İstanbul", code = "4")
                ) {}
            }
        }
    }
}
