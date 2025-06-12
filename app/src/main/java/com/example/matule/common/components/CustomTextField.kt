package com.example.matule.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matule.R
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.hint
import com.example.matule.ui.theme.text

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    onlyNumber: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(background),
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = hint,
                        fontSize = 20.sp
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = { newText ->
                        onValueChange(newText)
                    },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = text
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    singleLine = true,
                    readOnly = readOnly,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = if (onlyNumber) KeyboardType.Number else KeyboardType.Unspecified
                    )
                )
            }
        }
    }
}

@Composable
fun CustomTextFieldWithPassword(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(background),
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = hint,
                        fontSize = 20.sp
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = { onValueChange(it.take(30)) },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = text
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    maxLines = 1,
                    singleLine = true
                )
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off),
                    contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                    tint = hint
                )
            }
        }
    }
}