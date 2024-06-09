package com.example.virtuallibrary.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.virtuallibrary.api.ApiClient
import com.example.virtuallibrary.auth.AuthViewModel
import com.example.virtuallibrary.navigation.ROUTE_BOOK_DETAILS
import com.example.virtuallibrary.navigation.ROUTE_FAVOURITES
import com.example.virtuallibrary.navigation.ROUTE_LOGGED_OUT
import com.example.virtuallibrary.navigation.ROUTE_SEARCH
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel, query: String) {
    val viewModel = remember { BookViewModel(ApiClient.bookApiService) }
    val books by viewModel.books.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(query) {
        scope.launch {
            viewModel.fetchBooks(query)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    navController.navigate(ROUTE_SEARCH)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_FAVOURITES)
                    }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorites")
                    }
                }

                IconButton(onClick = {
                    authViewModel.signOut()
                    navController.navigate(ROUTE_LOGGED_OUT) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                }
            }
        }
    ) {
        Column(
        ) {
            Text(
                text = "Virtual Library",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start).padding(it).padding(start = 16.dp, bottom = 13.dp)
            )
            Divider(color = GreenColor, thickness = 1.dp)
            LazyColumn(modifier = Modifier
                .fillMaxSize()
            ) {
                items(books) { book ->
                    Log.d("HomeScreen", "Displaying book: ${book.volumeInfo.title}, ${book.id}")
                    BookItem(book = book, navController = navController)
                }
            }
        }
    }
}