package com.example.scribesnook.api

import com.example.scribesnook.LoginRequest
import com.example.scribesnook.LoginResponse
import com.example.scribesnook.SaveRatingRequest
import com.example.scribesnook.model.ShelfBook
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/* -----------------------------
   Backend Models
------------------------------ */

data class BackendBook(
    val _id: String,
    val googleId: String,
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val isbn10: String?,
    val thumbnailUrl: String?
)

data class CommunityReview(
    val googleId: String,
    val title: String?,
    val authors: List<String>?,
    val userRating: Float,
    val spicyRating: Float,
    val reviewText: String
)

/* -----------------------------
   Retrofit Service
------------------------------ */

interface BackendService {

    // 🔐 AUTH
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    // 🔎 SEARCH (optional backend search)
    @GET("books/search")
    suspend fun searchBooks(
        @Query("query") query: String
    ): Response<List<BackendBook>>

    // ⭐ SAVE / UPDATE RATING
    @POST("ratings")
    suspend fun saveRating(
        @Header("Authorization") authHeader: String,
        @Body body: SaveRatingRequest
    ): Response<Unit>

    // 📚 USER SHELF
    @GET("ratings")
    suspend fun getShelf(
        @Header("Authorization") authHeader: String
    ): Response<List<ShelfBook>>

    // 🌍 COMMUNITY REVIEWS FOR A BOOK
    @GET("ratings/{googleId}")
    suspend fun getReviewsForBook(
        @Path("googleId") googleId: String
    ): Response<List<CommunityReview>>
}

/* -----------------------------
   Retrofit Instance
------------------------------ */

object BackendApi {

    private const val BASE_URL =
        "https://scribesnook-backend.onrender.com/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: BackendService by lazy {
        retrofit.create(BackendService::class.java)
    }
}

