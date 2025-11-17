package com.pompa.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.LocalPompaColors
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.OpetColors
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaAppTopBar(
    provinceName: String,
    provinceCode: String,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.pompaColorPalette.topBarColors.background,
            contentColor = MaterialTheme.pompaColorPalette.topBarColors.content
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.pompaColorPalette.buttonColors.background,
                            contentColor = MaterialTheme.pompaColorPalette.textColors.buttonText
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = provinceCode,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Text(
                        text = provinceName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Image(
                    painter = painterResource(R.drawable.pompa_app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                        .clip(shape = RoundedCornerShape(4.dp)),
                )
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
                provinceName = "Istanbul",
                provinceCode = "34"
            )
        }
    }
}