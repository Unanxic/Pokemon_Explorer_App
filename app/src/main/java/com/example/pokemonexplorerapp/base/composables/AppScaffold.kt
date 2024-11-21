package com.example.pokemonexplorerapp.base.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pokemonexplorerapp.base.theme.PaperWhite

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit = { WhiteBackground() },
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = { Box(modifier = Modifier.navigationBarsPadding()) },
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Box {
        background()
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            containerColor = Color.Transparent,
            topBar = topBar,
            bottomBar = bottomBar
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
fun WhiteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperWhite)
    )
}