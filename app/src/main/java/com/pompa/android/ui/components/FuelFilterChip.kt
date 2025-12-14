package com.pompa.android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun FuelFilterChip(
    filter: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {

    val background = if (selected) {
        MaterialTheme.pompaColorPalette.chipColors.selectedBackground
    } else {
        MaterialTheme.pompaColorPalette.chipColors.unselectedBackground
    }

    val textColor = if (selected) {
        MaterialTheme.pompaColorPalette.chipColors.selectedTextColor
    } else {
        MaterialTheme.pompaColorPalette.chipColors.unselectedTextColor
    }

    val borderColor = if (selected) {
        MaterialTheme.pompaColorPalette.chipColors.selectedBorderColor
    } else {
        MaterialTheme.pompaColorPalette.chipColors.unselectedBorderColor
    }

    val animatedBackground by animateColorAsState(
        targetValue = background,
        animationSpec = tween(durationMillis = 180),
        label = ""
    )

    Card(
        modifier = modifier.safeClickable(
            indication = ClickIndication.None
        ) {
            onItemClick()
        },
        shape = CircleShape,
        border = BorderStroke(
            0.5.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedBackground
        )
    ) {
        Text(
            text = filter,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FuelFilterChipPrev() {
    PompaTheme {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FuelFilterChip(
                filter = "All",
                selected = true
            ) { }
            FuelFilterChip(
                filter = "Gasoline",
                selected = false
            ) { }
        }
    }
}