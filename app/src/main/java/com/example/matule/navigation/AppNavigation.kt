package com.example.matule.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.presenter.CartScreen
import com.example.matule.presenter.FavouriteScreen
import com.example.matule.presenter.ForgotPasswordScreen
import com.example.matule.presenter.LoginScreen
import com.example.matule.presenter.MainScreen
import com.example.matule.presenter.NotificationScreen
import com.example.matule.presenter.OrderInfoScreen
import com.example.matule.presenter.OrdersScreen
import com.example.matule.presenter.ProductScreen
import com.example.matule.presenter.ProfileScreen
import com.example.matule.presenter.RegistrationScreen
import com.example.matule.presenter.ResetPasswordScreen
import com.example.matule.presenter.SendOtpScreen
import com.example.matule.presenter.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    profileViewModel: ProfileViewModel
) {
    NavHost(
        navController, startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }) {
        composable("MainScreen") { MainScreen(navController, profileViewModel) }
        composable("LoginScreen") { LoginScreen(navController, profileViewModel) }
        composable("RegistrationScreen") { RegistrationScreen(navController, profileViewModel) }
        composable("ProfileScreen") { ProfileScreen(navController, profileViewModel) }
        composable("ForgotPasswordScreen") { ForgotPasswordScreen(navController) }
        composable("NotificationScreen") { NotificationScreen(navController, profileViewModel) }
        composable("CartScreen") { CartScreen(navController, profileViewModel) }
        composable("OrdersScreen") { OrdersScreen(navController, profileViewModel) }
        composable("FavouriteScreen") { FavouriteScreen(navController, profileViewModel) }
        composable("SettingsScreen") { SettingsScreen(navController, profileViewModel) }
        composable(
            "SendOtpScreen/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { stackEntry ->
            val email = stackEntry.arguments?.getString("email")
            SendOtpScreen(navController, email!!)
        }
        composable("ProductScreen/{id}",
            arguments = listOf(navArgument("id") {type = NavType.LongType})
        ) { stackEntry ->
            val id = stackEntry.arguments?.getLong("id")
            ProductScreen(navController, id!!)
        }
        composable("OrderInfoScreen/{id}",
            arguments = listOf(navArgument("id") {type = NavType.LongType})
        ) { stackEntry ->
            val id = stackEntry.arguments?.getLong("id")
            OrderInfoScreen(navController, id!!, profileViewModel)
        }
        composable(
            "ResetPasswordScreen/{email}/{token}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { stackEntry ->
            val email = stackEntry.arguments?.getString("email")
            val token = stackEntry.arguments?.getString("token")
            ResetPasswordScreen(navController, email!!, token!!)
        }
    }
}