package com.pompa.android.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaLoadingView(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = modifier.size(84.dp),
            shape = CircleShape
        ) {
            PompaLoadingAnimation()
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PompaLoadingViewPrev() {
    PompaTheme {
        PompaLoadingView(
            onDismiss = {}
        )
    }
}