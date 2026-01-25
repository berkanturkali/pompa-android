package com.pompa.android.features.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun SettingsItem(
    @DrawableRes icon: Int,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .safeClickable(indication = ClickIndication.Ripple) {
                onItemClick()
            }
            .background(Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.pompaColorPalette.backgroundColors.primary
                ),
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.pompaColorPalette.textColors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)

            ) {
                Text(
                    title,
                    color = MaterialTheme.pompaColorPalette.textColors.secondary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic
                    )
                )

                Text(
                    value,
                    color = MaterialTheme.pompaColorPalette.textColors.primary,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
private fun SettingsItemPrev() {
    PompaTheme {
        SettingsItem(icon = R.drawable.ic_location, title = "Title", value = "Value") {}
    }
}