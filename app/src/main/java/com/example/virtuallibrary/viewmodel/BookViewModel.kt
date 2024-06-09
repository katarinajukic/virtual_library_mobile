package com.example.virtuallibrary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.api.ApiClient
import com.example.virtuallibrary.api.BookApiService
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.VolumeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val apiService: BookApiService) : ViewModel() {
    constructor() : this(ApiClient.bookApiService)
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books

    fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getBooks(query, "AIzaSyC61_EptFmyLsF5I0-LEVE6jx_cPpwmtFo")
                if (response.isSuccessful) {
                    val booksList = response.body()?.items?.mapNotNull { item ->
                        val volumeInfo = item.volumeInfo
                        Book(
                            id = item.id,
                            volumeInfo = VolumeInfo(
                                title = volumeInfo.title ?: "",
                                authors = volumeInfo.authors ?: listOf(),
                                description = volumeInfo.description ?: "",
                                imageLinks = volumeInfo.imageLinks
                            )
                        )
                    } ?: emptyList()
                    _books.value = booksList
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }
}

