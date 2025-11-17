package com.pompa.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

    Dialog(
        onDismissRequest = { }
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.pompaColorPalette.backgroundColors.primary,
            border = BorderStroke(0.5.dp, MaterialTheme.pompaColorPalette.borderColor)
        ) {

            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = message ?: stringResource(id = R.string.something_went_wrong),
                    color = MaterialTheme.pompaColorPalette.backgroundColors.onPrimary,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(id = R.string.okay),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.pompaColorPalette.buttonColors.content,
                            shape = RoundedCornerShape(7.dp)
                        )
                        .padding(horizontal = 32.dp, vertical = 6.dp)
                        .clickable {
                            onOkayButtonClick()
                        },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.pompaColorPalette.backgroundColors.onSecondary
                )
            }
        }
    }

}

@Preview
@Composable
private fun PompaAppDialogPrev() {
    CompositionLocalProvider(LocalPompaColors provides OpetColors) {
        PompaTheme {
            PompaAppDialog(null) { }
        }
    }
}