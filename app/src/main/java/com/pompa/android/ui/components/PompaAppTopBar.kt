package com.pompa.android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.OpetColors
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaAppTopBar(
    showBackButton: Boolean,
    showSelectedProvince: Boolean,
    title: String,
    provinceName: String,
    provinceCode: String,
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
) {
    val backButtonSize = 24.dp
    val spaceBetween = 8.dp

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            color = MaterialTheme.pompaColorPalette.topBarColors.background,
            contentColor = MaterialTheme.pompaColorPalette.topBarColors.content
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .animateContentSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spaceBetween),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .animateContentSize()
                ) {
                    AnimatedVisibility(
                        visible = showBackButton,
                        enter = scaleIn(),
                        exit = slideOutHorizontally { -it } + fadeOut()
                    ) {
                        PompaAppBackButton(
                            modifier = Modifier.padding(4.dp),
                            onBackButtonClick = onBackButtonClick,
                            size = backButtonSize
                        )
                    }
                }


                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                AnimatedVisibility(
                    showSelectedProvince,
                    enter = slideInHorizontally { it },
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.pompaColorPalette.buttonColors.background,
                                contentColor = MaterialTheme.pompaColorPalette.textColors.buttonText
                            ),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = provinceCode,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Text(
                            text = provinceName,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.animateContentSize(),
                        )
                    }
                }
            }
            HorizontalDivider(
                color = MaterialTheme.pompaColorPalette.borderColor,
                thickness = 0.5.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
private fun PompaAppTopBarPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        PompaTheme {
            PompaAppTopBar(
                showBackButton = true,
                showSelectedProvince = true,
                title = "Pompa",
                provinceName = "Istanbul",
                provinceCode = "34",
                onBackButtonClick = {}
            )
        }
    }
}