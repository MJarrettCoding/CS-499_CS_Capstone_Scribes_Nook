package com.example.scribesnook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load

class BookAdapter(
    private val onBookClick: (BookItem) -> Unit
) : ListAdapter<BookItem, BookAdapter.BookViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.book_cover)
        private val title: TextView = itemView.findViewById(R.id.book_title)

        fun bind(book: BookItem) {
            title.text = book.volumeInfo?.title ?: "No Title"

            val imageUrl = book.volumeInfo?.imageLinks?.thumbnail
                ?.replace("http://", "https://")

            cover.load(imageUrl) {
                placeholder(R.drawable.ic_book_placeholder)
            }

            itemView.setOnClickListener { onBookClick(book) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(old: BookItem, new: BookItem) =
                old.id == new.id

            override fun areContentsTheSame(old: BookItem, new: BookItem) =
                old == new
        }
    }
}