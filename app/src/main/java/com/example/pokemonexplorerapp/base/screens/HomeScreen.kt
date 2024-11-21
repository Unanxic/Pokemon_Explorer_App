package com.example.pokemonexplorerapp.base.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.composables.AppScaffold
import com.example.pokemonexplorerapp.base.composables.GenericOutlinedTextField
import com.example.pokemonexplorerapp.base.composables.PokemonTypeDropdown
import com.example.pokemonexplorerapp.base.composables.TopBar
import com.example.pokemonexplorerapp.utils.pokeScaffoldPaddings

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    AppScaffold(
        topBar = {
            TopBar(
                title = "HomeScreen",
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pokeScaffoldPaddings(
                    paddingValues = paddingValues,
                    horizontalPadding = 10.dp,
                    scrollable = false
                )
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                GenericOutlinedTextField(
                    initialValue = "",
                    onValueChanged = { value ->
                        // Handle text change here

                    },
                    placeHolder = "e.g., Pikachu",
                    imeAction = ImeAction.Done,
                    trailingIconComposable = {
                        Image(
                            painter = painterResource(id = R.drawable.search_icon),
                            contentDescription = "Search Icon"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                PokemonTypeDropdown(
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}