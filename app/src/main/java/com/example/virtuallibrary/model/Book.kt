package com.example.virtuallibrary.model

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    var isFavorite: Boolean = false,
    var isRented: Boolean = false
)