package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.viewmodel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    // For demonstration, we allow passing login screen easily
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(viewModel = viewModel) {
                // In a real app, this waits for auth success. Here we just navigate.
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        }
        
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToGenerator = { type ->
                    navController.navigate("generator/$type")
                },
                onNavigateToWallet = {
                    navController.navigate(Routes.WALLET)
                }
            )
        }
        
        composable(
            route = Routes.GENERATOR,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "Website"
            GeneratorScreen(
                type = type,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToResult = {
                    navController.navigate("result/new") {
                        popUpTo(Routes.GENERATOR) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.RESULT) {
            ResultScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(Routes.HOME, false) }
            )
        }
        
        composable(Routes.WALLET) {
            WalletScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
