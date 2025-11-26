package com.pompa.android.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.pompaColorPalette.buttonColors.background,
    contentColor: Color = MaterialTheme.pompaColorPalette.buttonColors.content,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = backgroundColor
        ),
        onClick = {
            onClick()
        }) {

        Text(
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            text = stringResource(textId)
        )
    }


}

@Preview
@Composable
private fun PompaButtonPrev() {
    PompaTheme {
        PompaButton(
            textId = R.string.okay,
            onClick = { })
    }
}