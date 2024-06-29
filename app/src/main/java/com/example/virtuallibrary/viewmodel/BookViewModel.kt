package com.example.virtuallibrary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.api.ApiClient
import com.example.virtuallibrary.api.BookApiService
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.Favorite
import com.example.virtuallibrary.model.VolumeInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BookViewModel(private val apiService: BookApiService) : ViewModel() {
    constructor() : this(ApiClient.bookApiService)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> get() = _book

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            fetchFavorites(userId)
        }
    }

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
                    val userId = firebaseAuth.currentUser?.uid
                    if (userId != null) {
                        val favoriteSnapshot = firestore.collection("favorites")
                            .document(userId)
                            .collection("userFavorites")
                            .document(book.id)
                            .get()
                            .await()
                        book.isFavorite = favoriteSnapshot.exists()
                    }
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

    fun addFavorite(userId: String, book: Book) {
        viewModelScope.launch {
            try {
                val imageUrl = book.volumeInfo.imageLinks?.thumbnail ?: ""
                if (imageUrl.isEmpty()) {
                    Log.e("addFavorite", "No image URL available for book: ${book.volumeInfo.title}")
                }

                book.isFavorite = true


                val favorite = Favorite(
                    userId = userId,
                    bookId = book.id,
                    title = book.volumeInfo.title,
                    author = book.volumeInfo.authors.joinToString(", "),
                    imageUrl = imageUrl
                )

                firestore.collection("favorites")
                    .document(userId)
                    .collection("userFavorites")
                    .document(book.id)
                    .set(favorite)
                    .addOnSuccessListener {
                        Log.d("addFavorite", "Successfully added favorite: $favorite")
                        _favorites.value += favorite
                    }
                    .addOnFailureListener { e ->
                        Log.e("addFavorite", "Error adding favorite", e)
                    }
            } catch (e: Exception) {
                Log.e("addFavorite", "Exception in addFavorite", e)
            }
        }
    }

    fun removeFavorite(userId: String, bookId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("favorites")
                    .document(userId)
                    .collection("userFavorites")
                    .document(bookId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("removeFavorite", "Successfully removed favorite: $bookId")
                        _favorites.value = _favorites.value.filter { it.bookId != bookId }
                    }
                    .addOnFailureListener { e ->
                        Log.e("removeFavorite", "Error removing favorite", e)
                    }
            } catch (e: Exception) {
                Log.e("removeFavorite", "Exception in removeFavorite", e)
            }
        }
    }

    fun fetchFavorites(userId: String) {
        viewModelScope.launch {
            try {
                val favoritesList = mutableListOf<Favorite>()
                firestore.collection("favorites")
                    .document(userId)
                    .collection("userFavorites")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val favorite = document.toObject(Favorite::class.java)
                            favoritesList.add(favorite)
                        }
                        _favorites.value = favoritesList
                    }
                    .addOnFailureListener { exception ->
                        Log.e("fetchFavorites", "Error fetching favorites", exception)
                    }
            } catch (e: Exception) {
                Log.e("fetchFavorites", "Exception in fetchFavorites", e)
            }
        }
    }
}
