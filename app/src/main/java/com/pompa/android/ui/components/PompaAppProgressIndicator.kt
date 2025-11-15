package com.pompa.android.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pompa.android.ui.providers.pompaColorPalette

@Composable
fun PompaAppProgressIndicator(modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.pompaColorPalette.progressIndicatorColors.background,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.pompaColorPalette.progressIndicatorColors.indicator,
        )
    }
}