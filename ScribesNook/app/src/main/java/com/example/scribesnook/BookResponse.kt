package com.example.scribesnook

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookResponse(
    val items: List<BookItem>?
) : Parcelable

@Parcelize
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo?
) : Parcelable

@Parcelize
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val averageRating: Float?,
    val ratingsCount: Int?,
    val industryIdentifiers: List<IndustryIdentifier>? // Added for ISBN-10
) : Parcelable

@Parcelize
data class ImageLinks(
    val thumbnail: String?
) : Parcelable

@Parcelize
data class IndustryIdentifier(
    val type: String?,       // e.g., "ISBN_10", "ISBN_13"
    val identifier: String?  // actual ISBN number
) : Parcelable