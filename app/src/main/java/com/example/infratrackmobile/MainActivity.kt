package com.example.infratrackmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infratrackmobile.core.navigation.Screen
import com.example.infratrackmobile.core.ui.placeholder.PlaceholderScreen
import com.example.infratrackmobile.core.ui.theme.InfraTrackMobileTheme
import com.example.infratrackmobile.features.auth.domain.usecase.ObserveSessionUseCase
import com.example.infratrackmobile.features.auth.presentation.screen.LoginScreen
import com.example.infratrackmobile.features.auth.presentation.screen.ProfileScreen
import com.example.infratrackmobile.features.inspection.presentation.screen.AssignedInspectionsScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var observeSessionUseCase: ObserveSessionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfraTrackMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        observeSessionUseCase = observeSessionUseCase
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    observeSessionUseCase: ObserveSessionUseCase
) {
    val navController = rememberNavController()
    val session by observeSessionUseCase().collectAsState(initial = null)

    LaunchedEffect(session) {
        if (session != null) {
            navController.navigate(Screen.InspectionList) {
                popUpTo(Screen.InspectionList) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login,
        modifier = modifier
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.InspectionList) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen()
        }
        composable<Screen.InspectionList> {
            AssignedInspectionsScreen()
        }
        composable<Screen.WorkOrderList> {
            PlaceholderScreen("Work Order List")
        }
        composable<Screen.SyncStatus> {
            PlaceholderScreen("Sync Status")
        }
    }
}
