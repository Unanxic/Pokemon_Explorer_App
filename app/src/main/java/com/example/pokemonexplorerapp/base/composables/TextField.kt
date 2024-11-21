package com.example.pokemonexplorerapp.base.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonexplorerapp.base.theme.CoolGrey

@Composable
fun GenericOutlinedTextField(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    onValueChanged: (String) -> Unit,
    readOnly: Boolean = false,
    textFieldColors: AppColors = AppColors.DEFAULT,
    imeAction: ImeAction = ImeAction.Next,
    isErrorTextField: Boolean = false,
    isEnabled: Boolean = true,
    focusRequester: FocusRequester = FocusRequester(),
    trailingIconComposable: (@Composable (() -> Unit))? = null,
    keyBoardType: KeyboardType = KeyboardType.Text,
    placeHolder: String,
) {
    var textFieldValue by rememberSaveable { mutableStateOf(initialValue) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() } // Moved focusRequester inside the composable
    val isFocused = remember { mutableStateOf(false) }
    val errorVisible = isErrorTextField && (!isFocused.value && initialValue.isNotEmpty())

    val placeholderColor = when (textFieldColors) {
        AppColors.DEFAULT -> Black.copy(alpha = 0.5f)
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusEvent { focusState ->
                isFocused.value = focusState.hasFocus
            },
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onValueChanged(it) // Fixed the unresolved reference
        },
        singleLine = true,
        placeholder = {
            Text(
                text = placeHolder,
                color = placeholderColor,
                fontSize = 16.sp,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyBoardType
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (imeAction == ImeAction.Done) {
                    focusManager.clearFocus()
                }
            }
        ),
        shape = RoundedCornerShape(30.dp),
        readOnly = readOnly,
        colors = textFieldColors.colors.invoke(),
        enabled = isEnabled,
        trailingIcon = when {
            trailingIconComposable != null -> trailingIconComposable
            else -> null
        }
    )
}

enum class AppColors(val colors: @Composable () -> TextFieldColors) {
    DEFAULT(
        colors = {
           OutlinedTextFieldDefaults.colors(
               cursorColor = Black,
               disabledContainerColor = Color.Transparent,
               errorContainerColor = Color.Transparent,
               focusedContainerColor = Color.Transparent,
               unfocusedContainerColor = Color.Transparent,
               // Unfocused
               unfocusedBorderColor = CoolGrey,
               unfocusedLabelColor = White,
               unfocusedPlaceholderColor = Black.copy(alpha = .5f),
               unfocusedTextColor = Black,
               // Focused
               focusedBorderColor = CoolGrey,
               focusedLabelColor = White,
               focusedPlaceholderColor = Black.copy(alpha = 0.5f),
               focusedTextColor = Black,
               // Error
               errorBorderColor = Color.Transparent,
               errorLabelColor = Color.Transparent,
               errorPlaceholderColor = Color.Transparent,
               errorCursorColor = White,
               errorTextColor = White,
               // Disabled
               disabledBorderColor = White.copy(alpha = 0.2f),
               disabledLabelColor = CoolGrey,
               disabledPlaceholderColor = CoolGrey,
               disabledTextColor = CoolGrey,
            )
        }
    )
}