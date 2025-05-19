package com.example.matule.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.matule.common.CustomTextField
import com.example.matule.common.CustomTextFieldWithPassword

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Привет!",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 30.sp
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Заполните свои данные",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                )
            }
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(
                        text = "Email",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                    Spacer(Modifier.height(10.dp))
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "xyz@gmail.com"
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column {
                    Text(
                        text = "Email",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                    Spacer(Modifier.height(10.dp))
                    CustomTextFieldWithPassword(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "*********"
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "Востановить",
                            fontSize = 16.sp
                        )
                    }
                }
                Button(
                    onClick = {},
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Transparent,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Войти",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 18.sp
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Вы впервые?",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            TextButton(onClick = {
                navController.navigate("RegistrationScreen")
            }) {
                Text(
                    text = "Создать пользователя",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }
    }
}