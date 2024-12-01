package com.example.pokemonexplorerapp.base.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.base.theme.CarminePink


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    label: String,
    type: ButtonType = ButtonType.PRIMARY,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val mainColor by animateColorAsState(
        targetValue = if (!isEnabled) CarminePink.copy(alpha = 0.5f) else if (isPressed) CarminePink else CarminePink,
        label = ""
    )
    val textColor by animateColorAsState(
        targetValue = if (type == ButtonType.PRIMARY) Color.White else CarminePink,
        label = "",
    )

    Box(
        modifier = modifier
            .heightIn(min = 40.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(26.dp),
                color = mainColor
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (type == ButtonType.PRIMARY) mainColor
                else Color.Transparent
            )
            .clickable(
                enabled = isEnabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(12.dp)
            .testTag(label),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 14.sp
        )
    }
}

enum class ButtonType {
    PRIMARY,
    SECONDARY,
}