package com.example.pokemonexplorerapp.base.screens

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.BottomBar
import com.example.pokemonexplorerapp.base.composables.BottomBarItems

@Composable
fun AppHomeRoot(
    activeBottomBarItem: BottomBarItems,
    onActiveBottomBarItemChange: (BottomBarItems) -> Unit,
    navController: NavHostController,
) {
    AppScaffold(
        bottomBar = {
            BottomBar(
                currentSelection = activeBottomBarItem,
                onItemClicked = {
                    onActiveBottomBarItemChange(it)
                }
            )
        },
    ) { paddingValues ->
        Crossfade(
            targetState = activeBottomBarItem, label = "BottomNavigation"
        ) { navBarItemState ->
            when (navBarItemState) {
                BottomBarItems.HOME -> HomeScreen(
                    navController = navController
                )
                BottomBarItems.FAVORITES -> FavoritesScreen(
                    navController = navController
                )
            }
        }
    }
}