package com.example.matule.presenter

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.domain.view.OrderViewModel

@Composable
fun OrderInfoScreen(
    navController: NavController,
    orderId: Long,
    orderViewModel: OrderViewModel = viewModel()
){

}