package com.example.matule.presenter

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.domain.view.AuthViewModel

@Composable
fun SendOtpScreen (navController: NavController, email: String, viewModel: AuthViewModel = viewModel()) {
    Text(email)
}