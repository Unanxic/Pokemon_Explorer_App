package com.example.pokemonexplorerapp.base.theme.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

const val ANIMATION_DURATION = 200
val ANIMATION_EASING = FastOutSlowInEasing

fun <T> getDefaultTweenAnimationSpec() = tween<T>(
    durationMillis = ANIMATION_DURATION, easing = ANIMATION_EASING
)