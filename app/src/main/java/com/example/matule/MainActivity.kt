package com.example.matule

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.matule.common.components.BottomBar
import com.example.matule.data.AuthData
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.AuthViewModel
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.navigation.AppNavigation
import com.example.matule.ui.theme.block

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(viewModel: AuthViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var authData by remember { mutableStateOf<AuthData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        authData = preferencesManager.getAuthData()
        viewModel.refreshToken(preferencesManager)
        viewModel.startPeriodicTokenRefresh(preferencesManager)

        authData?.let {
            profileViewModel.getProfileInfo(preferencesManager)
        }

        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(block)
    ) {
        if (isLoading) {
            Popup(
                alignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(block)
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            val startDestination = if (authData == null) "LoginScreen" else "MainScreen"

            AppNavigation(navController, startDestination, profileViewModel)
        }
    }
}