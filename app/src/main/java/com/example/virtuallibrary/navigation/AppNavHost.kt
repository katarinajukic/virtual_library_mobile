package com.example.virtuallibrary.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.virtuallibrary.auth.AuthViewModel
import com.example.virtuallibrary.auth.LoggedOutScreen
import com.example.virtuallibrary.auth.LoginScreen
import com.example.virtuallibrary.auth.RegisterScreen
import com.example.virtuallibrary.screen.HomeScreen
import com.example.virtuallibrary.screen.SearchScreen
import com.example.virtuallibrary.viewmodel.BookViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_LOGGED_OUT) {
            LoggedOutScreen(
                onLoginClick = { navController.navigate(ROUTE_LOGIN) },
                onRegisterClick = { navController.navigate(ROUTE_SIGNUP) }
            )
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController, authViewModel, onLoginSuccess = {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            })
        }
        composable(ROUTE_SIGNUP) {
            RegisterScreen(navController, authViewModel, onRegisterSuccess = {
                navController.navigate(ROUTE_LOGIN) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            })
        }
        composable(ROUTE_HOME) {
            HomeScreen(navController, authViewModel)        }

        composable(ROUTE_SEARCH) {
            SearchScreen(navController, bookViewModel)
        }

        composable(ROUTE_FAVOURITES) {
            // Placeholder composable for the favorites screen
            Text(text = "Favorites Screen")
        }
    }
}
