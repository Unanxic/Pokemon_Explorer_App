package com.example.pokemonexplorerapp.base.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.theme.Dune
import com.example.pokemonexplorerapp.utils.setNoRippleClickable

@Composable
fun TopBar(
    title: String,
    onBackClick: () -> Unit = {},
    showBackButton: Boolean = false,
    showFavoriteIcon: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (showBackButton) {
            BackButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                onClick = onBackClick
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Dune,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (showFavoriteIcon){
            FavoriteIcon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                isFavorite = isFavorite,
                onClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun BackButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .testTag("back_button")
            .setNoRippleClickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_left),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 9.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
    }
}

@Composable
private fun FavoriteIcon(
    modifier: Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    val iconRes = if (isFavorite) R.drawable.favorite_filled else R.drawable.favorite
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
        modifier = modifier
            .testTag("favorite_icon")
            .setNoRippleClickable(onClick = onClick),
    )
}