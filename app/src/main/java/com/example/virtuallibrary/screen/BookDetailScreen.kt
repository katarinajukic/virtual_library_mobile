package com.example.virtuallibrary.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.virtuallibrary.api.ApiClient
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.ui.theme.comfortaaLight
import com.example.virtuallibrary.ui.theme.robotoBold
import com.example.virtuallibrary.ui.theme.robotoRegular
import com.example.virtuallibrary.viewmodel.BookViewModel
import com.example.virtuallibrary.viewmodel.FavoriteViewModel
import com.example.virtuallibrary.viewmodel.NotificationViewModel
import com.example.virtuallibrary.viewmodel.RentalViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookDetailsScreen(navController: NavController, bookId: String) {
    val bookViewModel = remember { BookViewModel(ApiClient.bookApiService) }
    val favoriteViewModel = remember { FavoriteViewModel() }
    val rentalViewModel = remember { RentalViewModel() }
    val notificationViewModel = remember { NotificationViewModel() }

    val book by bookViewModel.book.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
    val rentals by rentalViewModel.rentals.collectAsState()
    val availableCopies = 5

    val rentedCopies = rentals.count { it.bookId == bookId && it.status == "approved" }
    val userRental =
        rentals.find { it.bookId == bookId && it.userId == FirebaseAuth.getInstance().currentUser?.uid }
    val isAvailable = (availableCopies - rentedCopies) > 0

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        if (book == null) {
            scope.launch {
                try {
                    bookViewModel.fetchBookById(bookId)
                } catch (e: Exception) {
                    Log.e("BookDetailsScreen", "Error fetching book: ${e.message}", e)
                }
            }
        }
    }

    LaunchedEffect(book) {
        isFavorite = book?.isFavorite ?: false
    }

    if (book != null) {
        Log.d("BookDetailsScreen", "Book details: ${book?.volumeInfo}")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    book?.volumeInfo?.imageLinks?.thumbnail?.let { imageUrl ->
                        Image(
                            painter = rememberImagePainter(data = imageUrl),
                            contentDescription = "Book Thumbnail",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {
                        Text(
                            text = book?.volumeInfo?.title ?: "",
                            style = TextStyle(
                                fontFamily = comfortaaLight,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.W900
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = book?.volumeInfo?.authors?.joinToString(", ")
                                ?: "Unknown Author",
                            style = TextStyle(
                                fontFamily = comfortaaLight,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraLight
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = book?.volumeInfo?.description?.replace(Regex("<[^>]*>"), "")
                        ?: "No description available.",
                    style = TextStyle(
                        fontFamily = robotoRegular,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraLight
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Log.d("BookDetailsScreen", "Book description: ${book?.volumeInfo?.description}")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                        if (userRental == null) {
                            if (isAvailable) {
                                rentalViewModel.requestRental(userId, book!!, {
                                    notificationViewModel.showRentalNotification(
                                        context,
                                        "Rental Request Approved",
                                        "You have 3 days to pick up the book and have to return it by ${
                                            SimpleDateFormat(
                                                "yyyy-MM-dd",
                                                Locale.getDefault()
                                            ).format(
                                                Date(System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000))
                                            )
                                        }."
                                    )
                                }, { exception ->
                                    if (exception.message == "You have reached the maximum limit of 5 rentals.") {
                                        notificationViewModel.showRentalNotification(
                                            context,
                                            "Rental Limit Reached",
                                            "You cannot rent more than 5 books at a time."
                                        )
                                    } else {
                                        notificationViewModel.showRentalNotification(
                                            context,
                                            "Rental Request Failed",
                                            "Failed to request rental: ${exception.message}"
                                        )
                                    }
                                })
                            } else {
                                notificationViewModel.showRentalNotification(
                                    context,
                                    "Rental Not Available",
                                    "No copies available for rent."
                                )
                            }
                        } else {
                            rentalViewModel.cancelRental(userId, bookId, {
                                notificationViewModel.showRentalNotification(
                                    context,
                                    "Rental Request Canceled",
                                    "Your rental request has been canceled."
                                )
                            }, { exception ->
                                notificationViewModel.showRentalNotification(
                                    context,
                                    "Cancellation Failed",
                                    "Failed to cancel rental: ${exception.message}"
                                )
                            })
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenColor,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(6.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (userRental == null) "REQUEST RENTAL" else "CANCEL RENTAL REQUEST",
                        style = TextStyle(
                            fontFamily = robotoBold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button

                        if (isFavorite) {
                            favoriteViewModel.removeFavorite(userId, bookId)
                            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            favoriteViewModel.addFavorite(userId, book!!)
                            Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                        }
                        isFavorite = !isFavorite
                        Log.d("BookDetailsScreen", "isFavorite toggled: $isFavorite")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = GreenColor
                    ),
                    border = BorderStroke(2.dp, GreenColor),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(6.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (isFavorite) "REMOVE FROM FAVORITES" else "ADD TO FAVORITES",
                        style = TextStyle(
                            fontFamily = robotoBold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    } else {
        Text("Loading...", modifier = Modifier.padding(16.dp))
    }
}