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
import com.example.infratrackmobile.features.auth.presentation.screen.StartupScreen
import com.example.infratrackmobile.features.dashboard.presentation.screen.DashboardScreen
import com.example.infratrackmobile.features.inspection.presentation.screen.AssignedInspectionsScreen
import com.example.infratrackmobile.features.inspection.presentation.screen.InspectionDetailScreen
import com.example.infratrackmobile.features.workorder.presentation.screen.WorkOrderDetailScreen
import com.example.infratrackmobile.features.workorder.presentation.screen.WorkOrderListScreen
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
        // If we are on an authenticated screen and session becomes null, go to Login
        if (session == null && navController.currentDestination?.route != Screen.Login::class.qualifiedName) {
            navController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Startup,
        modifier = modifier
    ) {
        composable<Screen.Startup> {
            StartupScreen(
                onAuthenticated = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Startup) { inclusive = true }
                    }
                },
                onNoSession = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Startup) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Dashboard> {
            DashboardScreen(
                onAssignedInspectionsClick = {
                    navController.navigate(Screen.InspectionList)
                },
                onAssignedWorkOrdersClick = {
                    navController.navigate(Screen.WorkOrderList)
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen()
        }
        composable<Screen.InspectionList> {
            AssignedInspectionsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onInspectionClick = { inspectionId ->
                    navController.navigate(Screen.InspectionDetail(id = inspectionId.toString()))
                }
            )
        }
        composable<Screen.InspectionDetail> {
            InspectionDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.WorkOrderList> {
            WorkOrderListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onWorkOrderClick = { workOrderId ->
                    navController.navigate(Screen.WorkOrderDetail(id = workOrderId.toString()))
                }
            )
        }
        composable<Screen.WorkOrderDetail> {
            WorkOrderDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.SyncStatus> {
            PlaceholderScreen("Sync Status")
        }
    }
}
