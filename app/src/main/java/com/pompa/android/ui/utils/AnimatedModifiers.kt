package com.pompa.android.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer


fun Modifier.slideInOnFirstComposition(
    fromY: Float,
    durationMillis: Int = 300
): Modifier = composed {
    val anim = remember { Animatable(fromY) }

    LaunchedEffect(fromY) {
        anim.snapTo(fromY)
        anim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis, easing = LinearEasing)
        )
    }

    graphicsLayer(translationY = anim.value)
}

fun Modifier.slideInByScrollDirection(
    scrollingDown: Boolean,
    distancePx: Float = 300f,
    durationMillis: Int = 300,
): Modifier = slideInOnFirstComposition(
    fromY = if (scrollingDown) distancePx else -distancePx,
    durationMillis = durationMillis
)