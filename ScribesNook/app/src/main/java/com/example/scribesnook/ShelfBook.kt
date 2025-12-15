package com.example.scribesnook.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShelfBook(
    val googleId: String,          // ✅ source of truth
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val isbn10: String?,
    val thumbnailUrl: String?
) : Parcelable

@Parcelize
data class RatingReview(
    val bookId: String,            // equals googleId
    val userRating: Float,
    val spicyRating: Float,
    val reviewText: String
) : Parcelable