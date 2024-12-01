package com.example.pokemonexplorerapp.base.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.utils.PokemonFilterType
import com.example.pokemonexplorerapp.utils.capitalizeFirstLetter
import com.example.pokemonexplorerapp.utils.setNoRippleClickable

@Composable
fun PokemonCard(
    name: String,
    types: List<PokemonFilterType>,
    imageUrl: String,
    isFavorite: Boolean,
    onLikeClicked: (String, Boolean) -> Unit,
    onClick: () -> Unit
) {
    val mainType = types.first()
    require(types.isNotEmpty()) { "PokemonCard requires at least one type." }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .setNoRippleClickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = mainType.color.copy(alpha = 0.3f),
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text Section
            PokemonTextSection(
                name = name.capitalizeFirstLetter(),
                types = types,
                modifier = Modifier.weight(1f)
            )

            // Image Section
            PokemonImageCard(
                color = mainType.color,
                isFavorite = isFavorite,
                onFavoriteClick = { onLikeClicked(name, isFavorite) },
                imageUrl = imageUrl
            )
        }
    }
}

@Composable
private fun PokemonTextSection(
    name: String,
    types: List<PokemonFilterType>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 8.dp,
                bottom = 16.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Pokemon Name
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Pokemon Types
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            types.forEach { type ->
                PokemonTypeBadge(type = type)
            }
        }
    }
}

@Composable
private fun PokemonImageCard(
    imageUrl: String,
    color: Color,
    isFavorite: Boolean = false,
    onFavoriteClick: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                val favoriteIcon = if (isFavorite) R.drawable.favorite_filled else R.drawable.favorite
                Image(
                    painter = painterResource(id = favoriteIcon),
                    contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                    modifier = Modifier
                        .size(25.dp)
                        .setNoRippleClickable { onFavoriteClick(!isFavorite) }
                )
            }
        }
    }
}