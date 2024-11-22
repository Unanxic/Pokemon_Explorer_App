package com.example.pokemonexplorerapp.base.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.theme.PaperWhite
import com.example.pokemonexplorerapp.base.theme.animations.getDefaultTweenAnimationSpec
import com.example.pokemonexplorerapp.utils.setNoRippleClickable

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentSelection: BottomBarItems,
    onItemClicked: (BottomBarItems) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp)
            .padding(horizontal = 40.dp)
            .shadow(
                elevation = 2.dp, // Shadow elevation
                shape = RoundedCornerShape(60.dp), // Match the shape of the bar
                clip = false // Ensures shadow extends beyond the shape
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color = PaperWhite,
                    shape = RoundedCornerShape(60.dp)
                )
        ) {
            BottomBarItems.entries.forEach { item ->
                MinorIcon(
                    item = item,
                    isSelected = item == currentSelection,
                    onClick = onItemClicked,
                    modifier = Modifier.weight(1f) // Equal spacing for all items
                )
            }
        }
    }
}

@Composable
private fun MinorIcon(
    modifier: Modifier = Modifier,
    item: BottomBarItems,
    onClick: (BottomBarItems) -> Unit,
    isSelected: Boolean
) {
    // Use gray icons when not selected
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color(0xFF666C7E),
        animationSpec = getDefaultTweenAnimationSpec(),
        label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(50.dp)
            .setNoRippleClickable { onClick(item) }
    ) {
        Image(
            painter = painterResource(id = if (isSelected) item.iconResId else item.grayIconResId),
            contentDescription = null,
            modifier = Modifier.height(30.dp),
        )
        Text(
            text = if (isSelected) stringResource(id = item.labelResId).uppercase() else "",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

enum class BottomBarItems(
    @DrawableRes
    val iconResId: Int,
    @DrawableRes
    val grayIconResId: Int,
    @StringRes
    val labelResId: Int,
) {
    HOME(
        iconResId = R.drawable.pokeball,
        grayIconResId = R.drawable.pokeball_gray,
        labelResId = R.string.home,
    ),
    FAVORITES(
        iconResId = R.drawable.pokeheart,
        grayIconResId = R.drawable.pokeheart_gray,
        labelResId = R.string.favorites,
    ),
}