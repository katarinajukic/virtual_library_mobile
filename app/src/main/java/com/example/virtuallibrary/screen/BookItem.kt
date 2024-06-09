package com.example.virtuallibrary.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.virtuallibrary.R
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.ui.theme.GreenColor

@Composable
fun BookItem(book: Book) {
    Row(modifier = Modifier.padding(5.dp)) {
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
        Spacer(modifier = Modifier.width(1.dp))
        Column {
            Text(book.volumeInfo.title, style = MaterialTheme.typography.titleMedium)
            Text(book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Divider(color = GreenColor, thickness = 1.dp)
}