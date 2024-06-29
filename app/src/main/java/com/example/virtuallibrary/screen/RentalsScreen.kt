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
import com.example.virtuallibrary.viewmodel.BookViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RentalsScreen(navController: NavController, viewModel: BookViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val rentals by viewModel.rentals.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchRentals(userId)
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
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {
            Text(
                text = "Rentals",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 13.dp)
            )
            Divider(color = GreenColor, thickness = 1.dp)
            if (rentals.isEmpty()) {
                Text(
                    text = "No books in rentals list",
                    modifier = Modifier.padding(16.dp)
                )
                Log.d("RentalsScreen", "No books in rentals list")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    items(rentals) { rental ->
                        RentalItem(
                            book = Book(
                                id = rental.bookId,
                                volumeInfo = VolumeInfo(
                                    title = rental.title,
                                    authors = listOf(rental.author),
                                    description = "",
                                    imageLinks = ImageLinks(thumbnail = rental.imageUrl)
                                )
                            ),
                            navController = navController,
                            requestDate = rental.rentalDate,
                            endDate = rental.dueDate
                        )
                        Log.d("RentalsScreen", "Book in rentals list: $rental")
                    }
                }
            }
        }
    }
}