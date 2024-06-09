package com.example.virtuallibrary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.api.ApiClient
import com.example.virtuallibrary.api.ApiClient.bookApiService
import com.example.virtuallibrary.api.BookApiService
import com.example.virtuallibrary.model.AccessInfo
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.PdfInfo
import com.example.virtuallibrary.model.VolumeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val apiService: BookApiService) : ViewModel() {
    constructor() : this(ApiClient.bookApiService)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books

    // MutableStateFlow to hold a single book
    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> get() = _book

    fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getBooks(query, "AIzaSyC61_EptFmyLsF5I0-LEVE6jx_cPpwmtFo")
                if (response.isSuccessful) {
                    val booksList = response.body()?.items?.mapNotNull { item ->
                        val volumeInfo = item.volumeInfo
                        val bookId = item.id ?: ""
                        Log.d("BookViewModel", "Fetched book: $bookId - ${volumeInfo.title}")
                        Book(
                            id = bookId,
                            volumeInfo = VolumeInfo(
                                title = volumeInfo.title ?: "",
                                authors = volumeInfo.authors ?: listOf(),
                                description = volumeInfo.description ?: "",
                                imageLinks = volumeInfo.imageLinks
                            ),
                            accessInfo = AccessInfo(
                                pdf = PdfInfo(
                                    isAvailable = item.accessInfo?.pdf?.isAvailable ?: false,
                                    downloadLink = item.accessInfo?.pdf?.downloadLink
                                )
                            )
                        )
                    } ?: emptyList()
                    _books.value = booksList
                } else {
                    Log.e("BookViewModel", "API request failed: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("BookViewModel", "Error fetching books: ${e.message}", e)
            }
        }
    }

    suspend fun fetchBookById(bookId: String) {
        try {
            val response = apiService.getBookById(bookId, "AIzaSyC61_EptFmyLsF5I0-LEVE6jx_cPpwmtFo")
            if (response.isSuccessful) {
                val book = response.body()
                if (book != null) {
                    _book.value = book
                } else {
                    Log.e("BookViewModel", "Failed to fetch book by ID: $bookId")
                }
            } else {
                Log.e("BookViewModel", "Failed to fetch book by ID: $bookId. Error: ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Log.e("BookViewModel", "Error fetching book by ID: $bookId. ${e.message}", e)
        }
    }
}


