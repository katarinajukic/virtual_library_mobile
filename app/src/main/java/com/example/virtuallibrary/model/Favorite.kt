package com.example.virtuallibrary.model

data class Favorite(
    var userId: String = "",
    var bookId: String = "",
    var title: String = "",
    var author: String = "",
    val imageUrl: String = ""
)
