package com.pompa.android.features.provinces.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pompa.android.features.provinces.viewmodel.ProvincesScreenViewModel
import com.pompa.android.model.provinces.Province
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaAppProgressIndicator
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.OpetColors
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun ProvincesScreen(
    navigateToFuelBrandsScreen: () -> Unit,
    modifier: Modifier = Modifier,
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
            navigateToFuelBrandsScreen()
        },
        onErrorDialogDismiss = {
            viewModel.errorMessage = null
        },
        showLoading = viewModel.showLoading.value
    )

}

@Composable
fun ProvincesScreenContent(
    showLoading: Boolean,
    provinces: List<Province>,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onErrorDialogDismiss: () -> Unit = {},
    onProvinceSelected: (Province) -> Unit
) {

    val listState = rememberLazyListState()

    var lastIndex by remember { mutableIntStateOf(0) }
    var lastOffset by remember { mutableIntStateOf(0) }

    val animatedProgress = remember { Animatable(initialValue = 300f) }
    val opacityProgress = remember { Animatable(initialValue = 0f) }

    val isScrollingDown = remember {
        derivedStateOf {
            val currIndex = listState.firstVisibleItemIndex
            val currOffset = listState.firstVisibleItemScrollOffset

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

        LazyColumn(
            state = listState
        ) {
            items(provinces) { province ->
                ProvinceItem(
                    scrollingDown = isScrollingDown.value,
                    modifier = animationModifier,
                    province = province
                ) {
                    onProvinceSelected(province)
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
private const val TAG = "ProvincesScreen"
@Composable
fun ProvinceItem(
    scrollingDown: Boolean,
    province: Province,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {
    Log.i(TAG, "ProvinceItem: code: ${province.code}")
    val interactionSource = remember { MutableInteractionSource() }
    val animatedProgress =
        remember { Animatable(initialValue = if (scrollingDown) 300f else -300f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 0f,
            animationSpec = tween(300, easing = LinearEasing)
        )
    }
    val animatedModifier = modifier
        .fillMaxWidth()
        .graphicsLayer(translationY = animatedProgress.value)

    Card(
        modifier = animatedModifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onItemClick()
            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.pompaColorPalette.buttonColors.background,
                    contentColor = MaterialTheme.pompaColorPalette.textColors.buttonText
                ),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = province.code.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Text(
                text = province.name,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.pompaColorPalette.textColors.title
            )
        }
    }
}


@Preview
@Composable
private fun PompaAppProgressIndicatorPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        PompaTheme {
            PompaAppProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun ProvinceItemPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
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