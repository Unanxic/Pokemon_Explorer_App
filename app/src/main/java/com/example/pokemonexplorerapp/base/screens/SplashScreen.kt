package com.example.pokemonexplorerapp.base.screens

import android.window.SplashScreen
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.navigation.Screen
import com.example.pokemonexplorerapp.base.theme.MidnightBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    var isVisible by remember { mutableStateOf(false) }
    val fadeInTransition = updateTransition(targetState = isVisible, label = "fadeIn")

    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000)
        navController.navigate(Screen.HomeScreen.route) {
            popUpTo(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val imageAlpha by fadeInTransition.animateFloat(
            transitionSpec = {
                tween(durationMillis = 1000)
            }, label = ""
        ) {
            if (it) 1f else 0f
        }
        Image(
            painter = painterResource(id = R.drawable.pokedex_logo),
            contentDescription = null,
            modifier = Modifier
                .alpha(imageAlpha)
        )
    }
}