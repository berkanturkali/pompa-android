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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable
import com.pompa.android.util.titleCase

@Composable
fun PompaAppTopBar(
    showBackButton: Boolean,
    showSelectedProvince: Boolean,
    title: String,
    provinceName: String,
    provinceCode: String,
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
    onSelectedProvinceClick: () -> Unit,
) {
    val backButtonSize = 24.dp
    val spaceBetween = 8.dp

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 8.dp, horizontal = 6.dp),
            color = MaterialTheme.pompaColorPalette.topBarColors.background,
            contentColor = MaterialTheme.pompaColorPalette.topBarColors.content,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )

                AnimatedVisibility(
                    showSelectedProvince,
                    enter = slideInHorizontally { it },
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.safeClickable(indication = ClickIndication.None) {
                            onSelectedProvinceClick()
                        }
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryBackground,
                                contentColor = MaterialTheme.pompaColorPalette.buttonColors.filledPrimaryContent
                            ),
                            modifier = Modifier.size(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = provinceCode,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp
                                    ),
                                )
                            }
                        }

                        Text(
                            text = provinceName.titleCase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 8.sp
                            ),
                            modifier = Modifier.animateContentSize(),
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.pompaColorPalette.borderColor,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun PompaAppTopBarPrev() {
    PompaTheme {
        PompaAppTopBar(
            showBackButton = true,
            showSelectedProvince = true,
            title = "Pompa",
            provinceName = "Istanbul",
            provinceCode = "34",
            onBackButtonClick = {},
            onSelectedProvinceClick = {}
        )
    }
}