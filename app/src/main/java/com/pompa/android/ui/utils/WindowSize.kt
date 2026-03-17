package com.pompa.android.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

private val TABLET_MIN_WIDTH_DP = 840.dp

@Composable
fun isTabletLayout(): Boolean {
    val density = LocalDensity.current
    val widthDp = with(density) { LocalWindowInfo.current.containerSize.width.toDp() }
    return widthDp >= TABLET_MIN_WIDTH_DP
}
