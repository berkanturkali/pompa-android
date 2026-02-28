package com.pompa.android.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme

@Composable
fun PompaLoadingView(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.pompaColorPalette.pullToRefreshColor.container,
    progressColor: Color = MaterialTheme.pompaColorPalette.pullToRefreshColor.content,
    containerSize: Dp = 56.dp,
    indicatorSize: Dp = 30.dp,
    strokeWidth: Dp = 3.dp,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
    ) {
        Surface(
            modifier = modifier
                .size(containerSize),
            shape = CircleShape,
            color = backgroundColor
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(indicatorSize),
                    color = progressColor,
                    strokeWidth = strokeWidth,
                )
            }
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
