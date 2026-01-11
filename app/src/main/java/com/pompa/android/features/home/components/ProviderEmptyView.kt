package com.pompa.android.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun ProviderEmptyView(
    provider: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_off),
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
            Text(
                stringResource(
                    R.string.could_not_find_any_result_for_this_provider,
                    provider
                ),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview
@Composable
private fun ProviderEmptyViewPrev() {
    PompaTheme {
        ProviderEmptyView(
            provider = "Aytemiz"
        )
    }
}