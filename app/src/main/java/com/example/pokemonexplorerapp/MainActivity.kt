package com.example.pokemonexplorerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.pokemonexplorerapp.base.navigation.MainNavHost
import com.example.pokemonexplorerapp.base.theme.PokemonExplorerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PokemonExplorerAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MainNavHost(navController = rememberNavController())
                }
            }
        }
    }
}