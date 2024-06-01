package com.example.virtuallibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.virtuallibrary.auth.AuthViewModel
import com.example.virtuallibrary.auth.LoggedOutScreen
import com.example.virtuallibrary.auth.LoginScreen
import com.example.virtuallibrary.auth.RegisterScreen
import com.example.virtuallibrary.data.AuthRepository

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel
) {
    val authRepository = AuthRepository()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_LOGGED_OUT) {
            LoggedOutScreen(
                onLoginClick = { navController.navigate(ROUTE_LOGIN) },
                onRegisterClick = { navController.navigate(ROUTE_SIGNUP) }
            )
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController, authRepository, authViewModel::loginUser) {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
        composable(ROUTE_SIGNUP) {
            RegisterScreen(navController, authRepository, authViewModel::registerUser) {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
        composable(ROUTE_HOME) {
            // HomeScreen or any other authenticated screen
        }
    }
}
