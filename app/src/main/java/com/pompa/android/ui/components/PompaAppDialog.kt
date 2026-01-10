package com.pompa.android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pompa.android.R
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.OpetColors
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaAppDialog(
    message: String?,
    modifier: Modifier = Modifier,
    onOkayButtonClick: () -> Unit
) {

    if (message == null) return

    val visibility = remember(message) {
        androidx.compose.animation.core.MutableTransitionState(false).apply {
            targetState = true
        }
    }

    LaunchedEffect(visibility.isIdle, visibility.currentState) {
        if (visibility.isIdle && !visibility.currentState) {
            onOkayButtonClick()
        }
    }

    Dialog(onDismissRequest = {
        visibility.targetState = false
    }) {
        AnimatedVisibility(
            visibleState = visibility,
            enter =
                fadeIn(animationSpec = tween(120)) +
                        scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        ),
            exit =
                fadeOut(animationSpec = tween(120)) +
                        scaleOut(
                            animationSpec = tween(150, easing = LinearEasing)
                        )
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.pompaColorPalette.backgroundColors.primary,
                border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.pompaColorPalette.textColors.onBrand,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = R.string.okay),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.pompaColorPalette.buttonColors.filledSecondaryBackground,
                                shape = RoundedCornerShape(7.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                visibility.targetState = false
                            }
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.pompaColorPalette.textColors.onHighlight
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PompaAppDialogPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        PompaTheme {
            PompaAppDialog("Something went wrong") { }
        }
    }
}