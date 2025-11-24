package com.pompa.android.features.brands.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pompa.android.R
import com.pompa.android.features.brands.viewmodel.FuelBrandsScreenViewModel
import com.pompa.android.model.brands.Brand
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaButton
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.OpetColors
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun FuelBrandsScreen(
    modifier: Modifier = Modifier,
    viewModel: FuelBrandsScreenViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
) {

    FuelBrandsScreenContent(
        modifier = modifier,
        brands = viewModel.brands,
        selectedBrand = viewModel.selectedBrand,
        onItemClick = { brand ->
            if (brand == viewModel.selectedBrand) {
                viewModel.selectedBrand = null
                return@FuelBrandsScreenContent
            }
            viewModel.confirmButtonEnabled = viewModel.selectedBrand != brand
            viewModel.selectedBrand = brand
        },
        confirmButtonEnabled = viewModel.confirmButtonEnabled,
        onConfirmButtonClick = {
            viewModel.saveSelectedBrand()
            navigateToHomeScreen()
        },
        onErrorDialogDismiss = {
            viewModel.errorMessage = null
        },
        showLoading = viewModel.isLoading.value,
        errorMessage = viewModel.errorMessage?.asString(LocalContext.current),
        onBackButtonClick = {
        }
    )

}

@Composable
private fun FuelBrandsScreenContent(
    showLoading: Boolean,
    confirmButtonEnabled: Boolean,
    brands: List<Brand>,
    selectedBrand: Brand?,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onConfirmButtonClick: () -> Unit,
    onErrorDialogDismiss: () -> Unit,
    onBackButtonClick: () -> Unit,
    onItemClick: (Brand) -> Unit,
) {

    val buttonTransition =
        updateTransition(targetState = confirmButtonEnabled, label = "ButtonTransition")

    val backgroundColorOfConfirmButton by buttonTransition.animateColor(label = "bgColor") {
        if (it) MaterialTheme.pompaColorPalette.buttonColors.background else MaterialTheme.pompaColorPalette.buttonColors.background.copy(
            alpha = 0.5f
        )
    }

    val alphaOfConfirmButton by buttonTransition.animateFloat(label = "alpha") {
        if (it) 1f else 0.5f
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(brands) { brand ->
                    BrandItem(
                        brand = brand,
                        selectedBrand = selectedBrand
                    ) {
                        onItemClick(brand)
                    }
                }
            }


            PompaButton(
                modifier = Modifier.alpha(alpha = alphaOfConfirmButton),
                backgroundColor = backgroundColorOfConfirmButton,
                textId = R.string.confirm
            ) {
                onConfirmButtonClick()
            }
        }

        AnimatedVisibility(
            visible = showLoading,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.pompaColorPalette.backgroundColors.primary,

                )
        }

        AnimatedVisibility(
            visible = errorMessage != null,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(
                Alignment.Center
            )
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
fun BrandItem(
    brand: Brand,
    selectedBrand: Brand?,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(brand.logo)
            .crossfade(true)
            .build()
    )
    Card(
        modifier = modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (painter.state !is AsyncImagePainter.State.Error) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Text(
                    text = brand.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.pompaColorPalette.textColors.title
                )
            }

            AnimatedVisibility(
                visible = selectedBrand != null && brand != selectedBrand,
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

@Preview
@Composable
private fun BrandItemPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        PompaTheme {
            BrandItem(
                brand = Brand(
                    id = 1,
                    name = "Opet",
                    logo = "https://www.opet.com.tr/Content/Images/Logos/opet-logo.png"
                ),
                onItemClick = {},
                selectedBrand = Brand(
                    id = 2,
                    name = "Shell",
                    logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Shell_logo.svg/2560px-Shell_logo.svg.png"
                )
            )
        }
    }
}

