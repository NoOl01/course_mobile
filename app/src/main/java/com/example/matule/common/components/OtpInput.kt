package com.example.matule.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.red

@Composable
fun OtpInput(
    onFilled: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var code by remember { mutableStateOf("") }

    OutlinedTextField(
        value = code,
        onValueChange = {
            if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                code = it
                if (it.length == 6) {
                    focusManager.clearFocus()
                    onFilled(it)
                }
            }
        },
        modifier = Modifier
            .width(1.dp)
            .height(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 0.sp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            cursorColor = Color.Transparent
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (0 until 6).forEach { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        BorderStroke(
                            1.dp,
                            if (code.length == index) red else Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .height(60.dp)
                    .background(background)
                    .clickable {
                        focusRequester.requestFocus()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = code.getOrNull(index)?.toString() ?: "",
                    fontSize = 30.sp
                )
            }
        }
    }
}