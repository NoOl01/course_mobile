package com.example.matule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.AuthViewMode
import com.example.matule.navigation.AppNavigation
import com.example.matule.ui.theme.MatuleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatuleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(innerPadding)
                }
            }
        }
    }
}

@Composable
fun App(padding: PaddingValues, viewModel: AuthViewMode = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    val authState =
        preferencesManager.getAuthData.collectAsState(initial = arrayOf("", ""))

    val isDataLoaded = !authState.value.contentEquals(arrayOf("", ""))
    val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route // todo загатовка для BottomMenu

    LaunchedEffect(Unit) {
        scope.launch {
            //todo
            // рефреш при заходе
        }
    }

    Box(modifier = Modifier.padding(padding).background(MaterialTheme.colorScheme.background)) {
        if (!isDataLoaded) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            val startDestination =
                if (authState.value!![0].isNotBlank()) "MainScreen" else "LoginScreen"
            AppNavigation(navController, startDestination)
        }
    }
}