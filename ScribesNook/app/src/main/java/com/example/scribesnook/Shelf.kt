package com.example.scribesnook.model

import com.example.scribesnook.BookItem


data class Shelf(
    val id: String,
    val name: String,
    val books: MutableList<BookItem>
)