package com.pompa.android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun FuelFilterChip(
    filter: String,
    icon: Int,
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
        MaterialTheme.pompaColorPalette.chipColors.unselectedBorderColor.copy(0.5f)
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
        colors = CardDefaults.cardColors(
            containerColor = animatedBackground
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = filter,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = textColor,
            )
        }
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
                selected = true,
                icon = R.drawable.ic_apps
            ) { }
            FuelFilterChip(
                filter = "Gasoline",
                selected = false,
                icon = R.drawable.ic_apps
            ) { }
        }
    }
}