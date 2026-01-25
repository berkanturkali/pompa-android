package com.pompa.android.features.providers.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.pompa.android.BuildConfig
import com.pompa.android.R
import com.pompa.android.features.providers.viewmodel.ProvidersScreenViewModel
import com.pompa.android.model.brands.Provider
import com.pompa.android.ui.components.PompaAppDialog
import com.pompa.android.ui.components.PompaButton
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaColor
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.DeviceManager

@Composable
fun ProvidersScreen(
    modifier: Modifier = Modifier,
    viewModel: ProvidersScreenViewModel = hiltViewModel(),
    navigateUpOrToHomeScreen: () -> Unit,
) {

    ProvidersScreenContent(
        modifier = modifier,
        providers = viewModel.providers,
        selectedProvider = viewModel.selectedProvider,
        onItemClick = { provider ->
            if (provider == viewModel.selectedProvider) {
                viewModel.selectedProvider = null
                viewModel.confirmButtonEnabled = false
                return@ProvidersScreenContent
            }
            viewModel.confirmButtonEnabled =
                viewModel.selectedProvider != provider && !provider.name.equals(
                    viewModel.getFavoriteProviderName(),
                    ignoreCase = true
                )
            viewModel.selectedProvider = provider
        },
        confirmButtonEnabled = viewModel.confirmButtonEnabled,
        onConfirmButtonClick = {
            viewModel.saveSelectedProvider()
            navigateUpOrToHomeScreen()
        },
        onErrorDialogDismiss = {
            viewModel.errorMessage = null
        },
        showLoading = viewModel.isLoading.value,
        errorMessage = viewModel.errorMessage?.asString(LocalContext.current),
    )

}

@Composable
private fun ProvidersScreenContent(
    showLoading: Boolean,
    confirmButtonEnabled: Boolean,
    providers: List<Provider>,
    selectedProvider: Provider?,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onConfirmButtonClick: () -> Unit,
    onErrorDialogDismiss: () -> Unit,
    onItemClick: (Provider) -> Unit,
) {

    val buttonTransition =
        updateTransition(targetState = confirmButtonEnabled, label = "ButtonTransition")

    val backgroundColorOfConfirmButton by buttonTransition.animateColor(label = "bgColor") {
        if (it) MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryBackground else MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryBackground.copy(
            alpha = 0.5f
        )
    }

    val alphaOfConfirmButton by buttonTransition.animateFloat(label = "alpha") {
        if (it) 1f else 0.5f
    }

    Box(modifier = modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (list, confirmButton) = createRefs()

            LazyColumn(modifier = Modifier.constrainAs(list) {
                top.linkTo(parent.top)
                bottom.linkTo(confirmButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = androidx.constraintlayout.compose.Dimension.fillToConstraints
            }) {
                items(providers) { brand ->
                    ProviderItem(
                        isSelected = brand == selectedProvider,
                        modifier = Modifier.padding(8.dp),
                        provider = brand,
                        selectedProvider = selectedProvider
                    ) {
                        onItemClick(brand)
                    }
                }
            }

            PompaButton(
                modifier = Modifier
                    .alpha(alpha = alphaOfConfirmButton)
                    .constrainAs(confirmButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
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

        PompaAppDialog(message = errorMessage) {
            onErrorDialogDismiss()
        }
    }

}

@Composable
fun ProviderItem(
    isSelected: Boolean,
    provider: Provider,
    selectedProvider: Provider?,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {

    val context = LocalContext.current

    val imageUrl = if (DeviceManager.checkIfTheDeviceIsEmulator()) {
        provider.logo
            .replace(BuildConfig.IMAGE_BASE_URL, BuildConfig.EMULATOR_IMAGE_BASE_URL)
    } else {
        provider.logo
    }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        imageLoader = imageLoader
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onItemClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground.copy(0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground
                    ),
                    border = BorderStroke(
                        0.5.dp,
                        color = MaterialTheme.pompaColorPalette.borderColor
                    ),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (painter.state !is AsyncImagePainter.State.Error) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.padding(10.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_water),
                                contentDescription = null,
                            )
                        }
                    }
                }

                Text(
                    text = provider.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.pompaColorPalette.textColors.primary
                )
            }

            AnimatedVisibility(
                visible = selectedProvider != null && isSelected,
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
private fun ProviderItemPrev() {
    CompositionLocalProvider(LocalPompaColors provides PompaColor) {
        PompaTheme {
            ProviderItem(
                provider = Provider(
                    id = 1,
                    name = "Opet",
                    logo = "https://www.opet.com.tr/Content/Images/Logos/opet-logo.png"
                ),
                onItemClick = {},
                isSelected = true,
                selectedProvider = Provider(
                    id = 2,
                    name = "Shell",
                    logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Shell_logo.svg/2560px-Shell_logo.svg.png"
                )
            )
        }
    }
}

