package com.example.scribesnook

import com.example.scribesnook.api.BackendBook

fun BookItem.toBackendBook(): BackendBook {
    val isbn10 = volumeInfo?.industryIdentifiers
        ?.firstOrNull { it.type == "ISBN_10" }
        ?.identifier

    return BackendBook(
        _id = "", // backend assigns this
        googleId = id,
        title = volumeInfo?.title,
        authors = volumeInfo?.authors,
        description = volumeInfo?.description,
        isbn10 = isbn10,
        thumbnailUrl = volumeInfo?.imageLinks?.thumbnail
    )
}