package com.example.pokemonexplorerapp.base.composables

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.R
import com.example.pokemonexplorerapp.base.theme.CarminePink
import com.example.pokemonexplorerapp.utils.hideDialog
import com.example.pokemonexplorerapp.utils.setNoRippleClickable

@Composable
fun AppDialog(
    data: DialogData,
) {
    BackHandler {
        if (data.isSkippAble) hideDialog()
    }
    val rowModifier by remember(data) {
        derivedStateOf {
            if (data.secondaryButtonRes == null || data.primaryButtonRes == null) Modifier.width(
                IntrinsicSize.Min
            )
            else Modifier
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .setNoRippleClickable {
                if (data.isSkippAble) hideDialog()
            }
            .background(color = Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.setNoRippleClickable {}) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 190.dp)
                    .padding(horizontal = 30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(horizontal = 18.dp)
                    .padding(top = 50.dp, bottom = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .width(IntrinsicSize.Max)
                        .align(Alignment.TopCenter)
                ) {
                    if (data.textRes != null) {
                        Text(
                            text = stringResource(id = data.textRes),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    } else if (data.annotatedString != null) {
                        Text(
                            text = data.annotatedString,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .widthIn(min = 129.dp)
                            .then(rowModifier)
                            .height(IntrinsicSize.Min)
                    ) {
                        data.secondaryButtonRes?.let {
                            ButtonComponent(
                                label = stringResource(id = it),
                                type = ButtonType.SECONDARY,
                                onClick = {
                                    data.secondaryCallback?.invoke()
                                    if (data.autoHideOnSelection) {
                                        hideDialog()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                        data.primaryButtonRes?.let {
                            ButtonComponent(
                                label = stringResource(id = it),
                                type = ButtonType.PRIMARY,
                                onClick = {
                                    data.primaryCallback?.invoke()
                                    if (data.autoHideOnSelection) {
                                        hideDialog()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .offset(y = -(88 / 2).dp)
                    .setNoRippleClickable {}
                    .align(Alignment.TopCenter)
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(13.dp)
                    .clip(CircleShape)
                    .background(CarminePink),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = data.iconRes), contentDescription = "")
            }
        }
    }
}

sealed class DialogData(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val textRes: Int? = null,
    val annotatedString: AnnotatedString? = null,
    @StringRes
    val primaryButtonRes: Int? = null,
    @StringRes
    val secondaryButtonRes: Int? = null,
    val isSkippAble: Boolean = true,
    val primaryCallback: (() -> Unit)? = null,
    val secondaryCallback: (() -> Unit)? = null,
    val autoHideOnSelection: Boolean = true,
) {

    class RemoveFavorite(
        annotatedString: AnnotatedString,
        primaryCallback: () -> Unit,
        secondaryCallback: () -> Unit
    ) : DialogData(
        iconRes = R.drawable.heart,
        annotatedString = annotatedString,
        primaryButtonRes = R.string.remove,
        secondaryButtonRes = R.string.cancel,
        primaryCallback = primaryCallback,
        secondaryCallback = secondaryCallback,
        isSkippAble = false
    )
}