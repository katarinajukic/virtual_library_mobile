package com.example.virtuallibrary.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(navController: NavController, bookId: String) {
    Log.d("BookDetailsScreen", "Requested book ID: $bookId")
    val viewModel = remember { BookViewModel(ApiClient.bookApiService) }
    val book by viewModel.book.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        if (book == null) {
            scope.launch {
                try {
                    viewModel.fetchBookById(bookId)
                } catch (e: Exception) {
                    Log.e("BookDetailsScreen", "Error fetching book: ${e.message}", e)
                }
            }
        }
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
                            text = book?.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author",
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
                    text = book?.volumeInfo?.description?.replace(Regex("<[^>]*>"), "") ?: "No description available.",
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
                    onClick = { /* Handle Request Rental */ },
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
                        "REQUEST RENTAL",
                        style = TextStyle(
                            fontFamily = robotoBold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle Add to Favourites */ },
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
                        "ADD TO FAVOURITES",
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
                        val pdfDownloadLink = book?.accessInfo?.pdf?.downloadLink
                        if (pdfDownloadLink != null) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfDownloadLink))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "No PDF version available", Toast.LENGTH_SHORT).show()
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
                        "DOWNLOAD PDF VERSION",
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


