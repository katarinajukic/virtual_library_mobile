package com.example.virtuallibrary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.Rental
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RentalViewModel() : ViewModel() {
    private val _rentals = MutableStateFlow<List<Rental>>(emptyList())
    val rentals: StateFlow<List<Rental>> = _rentals

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            fetchRentals(userId)
        }
    }

    fun fetchRentals(userId: String) {
        viewModelScope.launch {
            try {
                val rentalsList = mutableListOf<Rental>()
                firestore.collection("rentals")
                    .document(userId)
                    .collection("userRentals")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val rental = document.toObject(Rental::class.java)
                            rentalsList.add(rental)
                        }
                        _rentals.value = rentalsList
                    }
                    .addOnFailureListener { exception ->
                        Log.e("fetchRentals", "Error fetching rentals", exception)
                    }
            } catch (e: Exception) {
                Log.e("fetchRentals", "Exception in fetchRentals", e)
            }
        }
    }

    fun requestRental(
        userId: String,
        book: Book,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val rentalDate = System.currentTimeMillis()
        val dueDate = rentalDate + (14 * 24 * 60 * 60 * 1000)

        viewModelScope.launch {
            try {
                val currentRentals = firestore.collection("rentals")
                    .document(userId)
                    .collection("userRentals")
                    .get()
                    .await()

                if (currentRentals.size() >= 5) {
                    throw Exception("You have reached the maximum limit of 5 rentals.")
                }

                val imageUrl = book.volumeInfo.imageLinks?.thumbnail ?: ""

                book.isRented = true

                val rental = Rental(
                    bookId = book.id,
                    userId = userId,
                    rentalDate,
                    dueDate,
                    status = "approved",
                    title = book.volumeInfo.title,
                    author = book.volumeInfo.authors.joinToString(", "),
                    imageUrl = imageUrl
                )

                firestore.collection("rentals")
                    .document(userId)
                    .collection("userRentals")
                    .document(book.id)
                    .set(rental)
                    .addOnSuccessListener {
                        Log.d("requestRental", "Successfully requested rental: $rental")
                        _rentals.value += rental
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("requestRental", "Error requesting rental", e)
                        onFailure(e)
                    }
            } catch (e: Exception) {
                Log.e("requestRental", "Exception in requestRental", e)
                onFailure(e)
            }
        }
    }

    fun cancelRental(
        userId: String,
        bookId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                firestore.collection("rentals")
                    .document(userId)
                    .collection("userRentals")
                    .document(bookId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("removeRental", "Successfully removed rental: $bookId")
                        _rentals.value = _rentals.value.filter { it.bookId != bookId }
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("removeRental", "Error removing rental", e)
                        onFailure(e)
                    }
            } catch (e: Exception) {
                Log.e("removeRental", "Exception in removeRental", e)
                onFailure(e)
            }
        }
    }
}
