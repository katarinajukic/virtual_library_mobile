package com.example.virtuallibrary.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.virtuallibrary.R
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.navigation.ROUTE_BOOK_DETAILS
import com.example.virtuallibrary.ui.theme.GreenColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RentalItem(
    book: Book,
    navController: NavController,
    requestDate: Long,
    endDate: Long
) {
    val scope = rememberCoroutineScope()

    Row(modifier = Modifier
        .padding(5.dp)
        .clickable {
            scope.launch {
                navController.navigate("$ROUTE_BOOK_DETAILS/${book.id}")
            }
        }
    ) {
        val thumbnailUrl = book.volumeInfo.imageLinks?.thumbnail

        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .placeholder(R.drawable.book_cover_placeholder)
                    .error(R.drawable.book_cover_placeholder)
                    .crossfade(true)
                    .build()
            ),
            contentDescription = "Book Thumbnail",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(book.volumeInfo.title, style = MaterialTheme.typography.titleMedium)
            Text(
                book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Request Date: ${
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(requestDate)
                    )
                }",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Due Date: ${
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(
                            endDate
                        )
                    )
                }",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    Divider(color = GreenColor, thickness = 1.dp)
    Log.d("RentalItem", "Clicked on book: ${book.id}")
}
