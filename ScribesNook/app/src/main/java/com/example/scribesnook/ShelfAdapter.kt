package com.example.scribesnook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scribesnook.BookItem
import com.example.scribesnook.R
import com.example.scribesnook.model.Shelf

class ShelfAdapter(
    private var shelves: List<Shelf>,
    private val onBookClick: (BookItem) -> Unit
) : RecyclerView.Adapter<ShelfAdapter.ShelfViewHolder>() {

    private val expandedShelves = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shelf, parent, false)
        return ShelfViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
        val shelf = shelves[position]
        val isExpanded = expandedShelves.contains(position)
        holder.bind(shelf, isExpanded)

        holder.itemView.setOnClickListener {
            if (isExpanded) {
                expandedShelves.remove(position)
            } else {
                expandedShelves.add(position)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = shelves.size

    fun updateShelves(newShelves: List<Shelf>) {
        shelves = newShelves
        notifyDataSetChanged()
    }

    inner class ShelfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shelfNameView: TextView = itemView.findViewById(R.id.shelf_name)
        private val booksContainer: ViewGroup = itemView.findViewById(R.id.books_container)

        fun bind(shelf: Shelf, expanded: Boolean) {
            shelfNameView.text = shelf.name
            booksContainer.visibility = if (expanded) View.VISIBLE else View.GONE
            booksContainer.removeAllViews()

            if (expanded) {
                shelf.books.forEach { book ->
                    val bookView = LayoutInflater.from(itemView.context)
                        .inflate(R.layout.item_book_in_shelf, booksContainer, false) as TextView
                    bookView.text = book.volumeInfo?.title ?: "No Title"
                    bookView.setOnClickListener {
                        onBookClick(book)
                    }
                    booksContainer.addView(bookView)
                }
            }
        }
    }
}