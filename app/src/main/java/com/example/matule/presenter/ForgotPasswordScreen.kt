package com.example.matule.presenter

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.R
import com.example.matule.common.components.CustomTextField
import com.example.matule.domain.view.AuthViewModel
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.disable
import com.example.matule.ui.theme.subTextLight
import com.example.matule.ui.theme.subtextDark
import com.example.matule.ui.theme.text
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var popUpIsActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyBoardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = background
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "назад"
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Забыл пароль",
                    color = text,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Введите свою учетную запись для сброса",
                    color = subtextDark,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
                Spacer(Modifier.height(60.dp))
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "xyz@gmail.com"
                )
                Spacer(Modifier.height(30.dp))
                Button(
                    enabled = email.isNotEmpty(),
                    onClick = {
                        keyBoardController?.hide()
                        scope.launch {
                            val error = viewModel.sendEmail(email)
                            if (error?.error == null) {
                                popUpIsActive = true
                                delay(3000)
                                navController.navigate("SendOtpScreen/$email")
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
                        text = "Отправить",
                        color = subTextLight,
                        fontSize = 18.sp
                    )
                }
            }
            if (popUpIsActive) {
                EmailCheckPopUp {
                    popUpIsActive = false
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun EmailCheckPopUp(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(block)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(50))
                            .background(accent)
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.email),
                            contentDescription = "Email icon",
                            tint = subTextLight,
                            modifier = Modifier.size(
                                30.dp
                            )
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Проверьте ваш Email",
                        fontSize = 24.sp,
                        color = text
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Мы отправили код восстановления пароля на вашу электронную почту.",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = subtextDark
                    )
                }
            }
        }
    }
}