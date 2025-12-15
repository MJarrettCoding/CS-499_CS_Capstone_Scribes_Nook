package com.example.scribesnook

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object GoogleBooksApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: BookService = retrofit.create(BookService::class.java)
}

interface BookService {
    @GET("volumes")
    fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 100, // max allowed per request
        @Query("orderBy") orderBy: String = "relevance",
        @Query("printType") printType: String = "books"
    ): Call<BookResponse>
    }

