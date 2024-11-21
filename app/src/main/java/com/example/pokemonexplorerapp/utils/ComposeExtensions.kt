package com.example.pokemonexplorerapp.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.pokeScaffoldPaddings(
    paddingValues: PaddingValues,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 0.dp,
    scrollState: ScrollState? = null,
    scrollStateEnabled: () -> Boolean = { true },
    scrollable: Boolean = true,
) = composed {
    this
        .padding(top = paddingValues.calculateTopPadding())
        .padding(horizontal = horizontalPadding)
        .then(
            if (scrollable)
                this.verticalScroll(
                    state = scrollState ?: rememberScrollState(),
                    enabled = scrollStateEnabled()
                )
            else
                this
        )
        .padding(vertical = verticalPadding)
        .padding(bottom = paddingValues.calculateBottomPadding())
        .padding(
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
        )
}