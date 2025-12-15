package com.example.scribesnook

data class SaveRatingRequest(
    val googleId: String,
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val isbn10: String?,
    val thumbnailUrl: String?,
    val userRating: Float,
    val spicyRating: Float,
    val reviewText: String
)