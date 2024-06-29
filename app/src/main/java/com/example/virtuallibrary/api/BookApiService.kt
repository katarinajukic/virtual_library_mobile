package com.example.virtuallibrary.api

import com.example.virtuallibrary.model.Book
import com.example.virtuallibrary.model.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Response<BookResponse>

    @GET("volumes/{bookId}")
    suspend fun getBookById(
        @Path("bookId") bookId: String,
        @Query("key") apiKey: String
    ): Response<Book>
}