package com.example.virtuallibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.virtuallibrary.auth.AuthViewModel
import com.example.virtuallibrary.auth.LoggedOutScreen
import com.example.virtuallibrary.auth.LoginScreen
import com.example.virtuallibrary.auth.RegisterScreen
import com.example.virtuallibrary.screen.BookDetailsScreen
import com.example.virtuallibrary.screen.FavouritesScreen
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
                navController.navigate(ROUTE_SEARCH) {
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
        composable(route = "$ROUTE_HOME?q={query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            HomeScreen(navController, authViewModel, query)
        }

        composable(ROUTE_SEARCH) {
            SearchScreen(navController, bookViewModel) { query ->
                navController.navigate(route = "$ROUTE_HOME?q=$query") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }

        composable(route = "$ROUTE_BOOK_DETAILS/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailsScreen(navController, bookId)
        }


        composable(ROUTE_FAVOURITES) {
            FavouritesScreen(navController, bookViewModel)
        }
    }
}
