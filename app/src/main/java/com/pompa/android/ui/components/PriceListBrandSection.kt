package com.pompa.android.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.resolveBackendAssetUrl

@Composable
fun PriceListBrandSection(
    name: String,
    logo: String,
    averagePrice: String?,
    isHeaderPinned: Boolean,
    showDivider: Boolean,
    isFavorite: Boolean,
    showInfoMessage: Boolean,
    modifier: Modifier = Modifier,
) {
    val headerHorizontalPadding by animateDpAsState(
        targetValue = if (isHeaderPinned) 0.dp else 8.dp,
        label = "headerHorizontalPadding"
    )

    val headerChildRowHorizontalPadding by animateDpAsState(
        targetValue = if (isHeaderPinned) 10.dp else 8.dp,
        label = "headerHorizontalPadding"
    )


    val headerChildRowVerticalPadding by animateDpAsState(
        targetValue = if (isHeaderPinned) 8.dp else 0.dp,
        label = "headerHorizontalPadding"
    )

    val headerParentVerticalPadding by animateDpAsState(
        targetValue = if (isHeaderPinned) 0.dp else 8.dp,
        label = "headerHorizontalPadding"
    )

    val context = LocalContext.current

    val imageUrl = resolveBackendAssetUrl(logo)

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

    val borderColor = MaterialTheme.pompaColorPalette.borderColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = headerHorizontalPadding, vertical = headerParentVerticalPadding)
            .background(MaterialTheme.pompaColorPalette.backgroundColors.primary)
            .then(
                if (isHeaderPinned && showDivider) {
                    Modifier.drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            color = borderColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(
                        horizontal = headerChildRowHorizontalPadding,
                        vertical = headerChildRowVerticalPadding
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
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
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (painter.state !is AsyncImagePainter.State.Error) {
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                        .clip(CircleShape),
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_water),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp),
                                    tint = MaterialTheme.pompaColorPalette.textColors.primary
                                )
                            }
                        }
                    }

                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.pompaColorPalette.textColors.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    if (isFavorite) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = Color(0xffFFD700),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.pompaColorPalette.cardColors.primaryBackground.copy(
                            0.8f
                        )
                    ),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        0.5.dp,
                        color = MaterialTheme.pompaColorPalette.borderColor.copy(0.8f)
                    )
                ) {
                    averagePrice?.let {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.pompaColorPalette.backgroundColors.primary)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.average),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.pompaColorPalette.textColors.primary.copy(
                                    alpha = 0.7f
                                )
                            )

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                            .toSpanStyle()
                                    ) {
                                        append(averagePrice)
                                    }
                                    withStyle(
                                        MaterialTheme.typography.labelSmall
                                            .toSpanStyle()
                                    ) {
                                        append("₺")
                                    }
                                },
                                color = MaterialTheme.pompaColorPalette.textColors.primary
                            )
                        }
                    }

                }
            }
            if (showInfoMessage) {
                Text(
                    stringResource(R.string.prices_may_vary_for_this_station),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        fontSize = 9.sp
                    ),
                    color = MaterialTheme.pompaColorPalette.textColors.primary.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = if (isHeaderPinned) 8.dp else 0.dp,
                            end = 12.dp,
                            top = if (isHeaderPinned) 0.dp else 4.dp
                        ),
                    textAlign = TextAlign.End
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PriceListBrandSectionPrev() {
    PompaTheme {
        PriceListBrandSection(
            name = "Shell",
            logo = "",
            averagePrice = "42.15",
            isHeaderPinned = true,
            isFavorite = true,
            showDivider = true,
            showInfoMessage = true
        )
    }
}
