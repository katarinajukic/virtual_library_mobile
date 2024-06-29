package com.example.virtuallibrary.model

data class Rental(
    val bookId: String = "",
    val userId: String = "",
    val rentalDate: Long = 0L,
    val dueDate: Long = 0L,
    val status: String = "",
    var title: String = "",
    var author: String = "",
    val imageUrl: String = ""
)
