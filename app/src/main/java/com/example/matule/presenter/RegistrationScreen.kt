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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.common.components.CustomTextField
import com.example.matule.common.components.CustomTextFieldWithPassword
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.AuthViewModel
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.disable
import com.example.matule.ui.theme.red
import com.example.matule.ui.theme.subTextLight
import com.example.matule.ui.theme.subtextDark
import com.example.matule.ui.theme.text
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var check by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp)
            .background(block),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Регистрация",
                    color = text,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Заполните свои данные",
                    color = subtextDark,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
            }
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorText,
                    color = red,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Column {
                    Text(
                        text = "Ваше имя",
                        color = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                    Spacer(Modifier.height(10.dp))
                    CustomTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "XXXXXXXXX"
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column {
                    Text(
                        text = "Email",
                        color = text,
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
                        text = "Пароль",
                        color = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight
                    )
                    Spacer(Modifier.height(10.dp))
                    CustomTextFieldWithPassword(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "●●●●●●●●"
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = check,
                        onCheckedChange = { check = it }
                    )
                    TextButton(onClick = {}) {
                        Text(
                            text = "Даю согласие на обработку Персональных данных"
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && check,
                    onClick = {
                        scope.launch {
                            val result = viewModel.registration(name, email, password,  preferencesManager)
                            if (result.error != null){
                                if (result.error == "User already exists"){
                                    errorText = "Такой пользователь уже существует"
                                }
                                errorText = "Произошла ошибка"
                            } else {
                                navController.navigate("LoginScreen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    colors = ButtonColors(
                        containerColor = accent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = disable,
                        disabledContentColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Зарегистрироваться",
                        color = subTextLight,
                        fontSize = 18.sp
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Есть аккаунт?",
                color = subtextDark,
                fontSize = 16.sp
            )
            TextButton(onClick = {
                navController.navigate("LoginScreen")
            }) {
                Text(
                    text = "Войти",
                    color = accent,
                    fontSize = 16.sp
                )
            }
        }
    }
}