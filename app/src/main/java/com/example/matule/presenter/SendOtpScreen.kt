package com.example.matule.presenter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.R
import com.example.matule.common.components.OtpInput
import com.example.matule.domain.view.AuthViewModel
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.subTextLight
import com.example.matule.ui.theme.subtextDark
import com.example.matule.ui.theme.text
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@Composable
fun SendOtpScreen(
    navController: NavController,
    email: String,
    viewModel: AuthViewModel = viewModel()
) {
    var timer by remember { mutableIntStateOf(120) }
    var otp by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (timer > 0) {
            delay(1000)
            timer -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
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
                    text = "OTP Проверка",
                    color = text,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Пожалуйста, проверьте свою электронную почту чтобы увидеть код подтверждения",
                    color = subtextDark,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
            }
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "OTP Код",
                    fontSize = 32.sp
                )
                Spacer(Modifier.height(10.dp))
                OtpInput { code ->
                    scope.launch {
                        val result = viewModel.checkOtp(email, code.toInt())
                        if (result.error == null) {
                            navController.navigate("ResetPasswordScreen/$email/${result.result}")
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    scope.launch {
                        timer = 200
                        viewModel.sendEmail(email)
                    }
                }) {
                    Text(
                        text = "Отправить заново",
                        fontSize = 18.sp,
                        color = if (timer != 0) subTextLight else subtextDark
                    )
                }
                Text(
                    text = String.format("%02d:%02d", timer / 60, timer % 60),
                    fontSize = 18.sp
                )
            }
        }
        Spacer(Modifier.weight(1f))
    }
}