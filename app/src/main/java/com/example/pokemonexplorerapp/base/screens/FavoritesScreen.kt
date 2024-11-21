package com.example.pokemonexplorerapp.base.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.TopBar

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    AppScaffold(
        topBar = {
            TopBar(
                title = "Favorites",
            )
        }
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                )
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
            )
        ) {

        }

    }
}