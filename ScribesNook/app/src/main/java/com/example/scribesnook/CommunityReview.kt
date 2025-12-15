package com.example.scribesnook

data class CommunityReview(
    val googleId: String,
    val title: String?,
    val authors: List<String>?,
    val userRating: Float,
    val spicyRating: Float,
    val reviewText: String
)
