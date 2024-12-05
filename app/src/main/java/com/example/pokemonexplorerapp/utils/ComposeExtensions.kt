package com.example.pokemonexplorerapp.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.setNoRippleClickable(
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit,
): Modifier = if (isEnabled)
    this.clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        onClick()
    }
else
    this