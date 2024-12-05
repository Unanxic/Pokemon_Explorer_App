package com.example.pokemonexplorerapp.utils

import com.example.pokemonexplorerapp.base.navigation.Screen

/**
 * This method returns a [Screen] which should be the next screen to navigate to.
 * Also skips any screen that need skipping.
 */
suspend fun decideWhereToNavigateNext(currentScreen: Screen? = null): Screen {

    val sortedMapOfScreens: Map<Int, Screen> = mapOf(
        0 to Screen.Splash,
        1 to Screen.HomeScreen,
    )
    val currentScreenEntry = sortedMapOfScreens.entries.find { it.value == currentScreen }
    return currentScreenEntry?.let { currentScreenFound ->
        sortedMapOfScreens
            .minus(0..currentScreenFound.key)
        return Screen.HomeScreen
    } ?: run {
        // If currentScreenEntry is null, we should show the Splash screen
        Screen.Splash
    }
}

