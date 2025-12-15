package com.example.scribesnook.data

import com.example.scribesnook.BookItem
import com.example.scribesnook.model.Shelf
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object ShelfManager {

    const val READ_SHELF_ID = "shelf_read"
    const val TBR_SHELF_ID = "shelf_tbr"

    // Thread-safe shelf storage only
    private val shelves = ConcurrentHashMap<String, Shelf>()

    init {
        shelves[READ_SHELF_ID] = Shelf(READ_SHELF_ID, "Read Shelf", mutableListOf())
        shelves[TBR_SHELF_ID] = Shelf(TBR_SHELF_ID, "TBR Shelf", mutableListOf())
    }

    fun getShelves(): List<Shelf> =
        shelves.values.toList()

    fun createShelf(name: String): Shelf {
        val id = UUID.randomUUID().toString()
        val newShelf = Shelf(id, name, mutableListOf())
        shelves[id] = newShelf
        return newShelf
    }

    fun addBookToShelf(book: BookItem, shelfId: String) {
        val shelf = shelves[shelfId] ?: return
        if (!shelf.books.any { it.id == book.id }) {
            shelf.books.add(book)
        }
    }

    fun removeBookFromShelf(book: BookItem, shelfId: String) {
        shelves[shelfId]?.books?.removeAll { it.id == book.id }
    }
}