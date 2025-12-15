package com.example.scribesnook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.scribesnook.R
import com.example.scribesnook.api.BackendBook

class CommunityBookAdapter(
    private val onBookClick: (BackendBook) -> Unit
) : ListAdapter<BackendBook, CommunityBookAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.book_cover)
        private val title: TextView = itemView.findViewById(R.id.book_title)

        fun bind(book: BackendBook) {
            title.text = book.title ?: "No Title"

            val imageUrl = book.thumbnailUrl?.replace("http://", "https://")
            cover.load(imageUrl) {
                placeholder(R.drawable.ic_book_placeholder)
            }

            itemView.setOnClickListener { onBookClick(book) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BackendBook>() {
            override fun areItemsTheSame(old: BackendBook, new: BackendBook) =
                old.googleId == new.googleId

            override fun areContentsTheSame(old: BackendBook, new: BackendBook) =
                old == new
        }
    }
}
