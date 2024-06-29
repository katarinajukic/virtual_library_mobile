package com.example.virtuallibrary.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.ImageLinks
import com.example.virtuallibrary.model.VolumeInfo
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.viewmodel.FavoriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavouritesScreen(navController: NavController, viewModel: FavoriteViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchFavorites(userId)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Favourites",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 13.dp)
            )
            Divider(color = GreenColor, thickness = 1.dp)
            if (favorites.isEmpty()) {
                Text(
                    text = "No books in favorites list",
                    modifier = Modifier.padding(16.dp)
                )
                Log.d("FavouritesScreen", "No books in favorites list")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    items(favorites) { favorite ->
                        BookItem(
                            book = Book(
                                id = favorite.bookId,
                                volumeInfo = VolumeInfo(
                                    title = favorite.title,
                                    authors = listOf(favorite.author),
                                    description = "",
                                    imageLinks = ImageLinks(thumbnail = favorite.imageUrl)
                                )
                            ),
                            navController = navController
                        )
                        Log.d("FavouritesScreen", "Book in favorites list: $favorite")
                    }
                }
            }
        }
    }
}