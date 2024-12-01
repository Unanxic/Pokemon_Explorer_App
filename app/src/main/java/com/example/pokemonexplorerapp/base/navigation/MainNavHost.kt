package com.example.pokemonexplorerapp.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokemonexplorerapp.base.composables.BottomBarItems
import com.example.pokemonexplorerapp.base.screens.AppHomeRoot
import com.example.pokemonexplorerapp.base.screens.PokemonDetailsScreen
import com.example.pokemonexplorerapp.base.screens.SplashScreen
import com.example.pokemonexplorerapp.base.theme.animations.slideInHorizontallyTransition
import com.example.pokemonexplorerapp.base.theme.animations.slideInOnReEnterHorizontallyTransition
import com.example.pokemonexplorerapp.base.theme.animations.slideOutHorizontallyTransition
import com.example.pokemonexplorerapp.base.theme.animations.slideOutOnReEnterHorizontallyTransition
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import com.example.pokemonexplorerapp.utils.addRouteParams
import com.example.pokemonexplorerapp.utils.decideWhereToNavigateNext
import kotlinx.coroutines.runBlocking

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    var activeBottomBarItem by remember { mutableStateOf(BottomBarItems.HOME) }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = runBlocking { decideWhereToNavigateNext().route },
        exitTransition = slideOutOnReEnterHorizontallyTransition,
        enterTransition = slideInHorizontallyTransition,
        popExitTransition = slideOutHorizontallyTransition,
        popEnterTransition = slideInOnReEnterHorizontallyTransition,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            AppHomeRoot(
                activeBottomBarItem = activeBottomBarItem,
                onActiveBottomBarItemChange = { newTabSelected: BottomBarItems ->
                    activeBottomBarItem = newTabSelected
                },
                navController = navController
            )
        }
        composable(
            route = Screen.PokemonDetails.route.addRouteParams(
                "name" to "name",
                "types" to "types",
                "imageUrl" to "imageUrl"
            ),
            arguments = listOf(
                navArgument("name") { defaultValue = "" },
                navArgument("types") { defaultValue = "" }, // Default to an empty string
                navArgument("imageUrl") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name").orEmpty()
            val types = backStackEntry.arguments?.getString("types")
                ?.split(",") // Split the comma-separated string
                ?.mapNotNull { typeName ->
                    PokemonFilterType.entries.find { it.name == typeName } // Safely map to `PokemonType`
                } ?: emptyList() // Fallback to an empty list if parsing fails
            val imageUrl = backStackEntry.arguments?.getString("imageUrl").orEmpty()

            PokemonDetailsScreen(
                pokemonName = name,
                pokemonType = types,
                imageUrl = imageUrl,
                navController = navController
            )
        }
    }
}