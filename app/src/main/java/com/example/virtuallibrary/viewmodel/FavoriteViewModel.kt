package com.example.virtuallibrary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.Favorite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel() : ViewModel() {
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

    fun addFavorite(userId: String, book: Book) {
        viewModelScope.launch {
            try {
                val imageUrl = book.volumeInfo.imageLinks?.thumbnail ?: ""
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
}
