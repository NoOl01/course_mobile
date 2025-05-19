package com.example.matule.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.matule.presenter.LoginScreen
import com.example.matule.presenter.MainScreen
import com.example.matule.presenter.RegistrationScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController, startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }) {
        composable("MainScreen") { MainScreen() }
        composable("LoginScreen") { LoginScreen(navController) }
        composable("RegistrationScreen") { RegistrationScreen(navController) }
    }
}