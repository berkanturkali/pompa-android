package com.pompa.android.util

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

sealed interface ClickIndication {
    data object None : ClickIndication
    data object Ripple : ClickIndication
    data class Custom(val factory: IndicationNodeFactory) : ClickIndication
}

@Composable
fun Modifier.safeClickable(
    enabled: Boolean = true,
    indication: ClickIndication = ClickIndication.Ripple,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier {
    val interaction = remember { MutableInteractionSource() }

    val resolvedIndication: IndicationNodeFactory? = when (indication) {
        ClickIndication.None -> null
        ClickIndication.Ripple -> ripple()
        is ClickIndication.Custom -> indication.factory
    }

    return this.clickable(
        enabled = enabled,
        interactionSource = interaction,
        indication = resolvedIndication,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )
}
