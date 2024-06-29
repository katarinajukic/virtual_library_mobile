package com.example.virtuallibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.virtuallibrary.auth.AuthViewModel
import com.example.virtuallibrary.navigation.AppNavHost
import com.example.virtuallibrary.navigation.ROUTE_LOGGED_OUT
import com.example.virtuallibrary.ui.theme.VirtualLibraryTheme
import com.example.virtuallibrary.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualLibraryApp()
        }
    }

    @Composable
    fun VirtualLibraryApp() {
        val navController = rememberNavController()

        Surface(color = Color.White) {
            VirtualLibraryTheme {
                AppNavHost(navController = navController, startDestination = ROUTE_LOGGED_OUT, authViewModel = authViewModel, bookViewModel = bookViewModel)
            }
        }
    }
}

