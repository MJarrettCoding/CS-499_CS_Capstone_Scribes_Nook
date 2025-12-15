package com.example.scribesnook.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import coil.load
import com.example.scribesnook.R
import com.example.scribesnook.data.ShelfManager

class BookDetailsFragment : Fragment() {

    private var isDescriptionExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val book = BookDetailsFragmentArgs
            .fromBundle(requireArguments())
            .googleBook

        val titleView = view.findViewById<TextView>(R.id.book_title)
        val authorView = view.findViewById<TextView>(R.id.book_authors)
        val descriptionLabel = view.findViewById<TextView>(R.id.description_label)
        val descriptionView = view.findViewById<TextView>(R.id.book_description)
        val expandDescription = view.findViewById<TextView>(R.id.expand_description)
        val isbnView = view.findViewById<TextView>(R.id.book_isbn)
        val ratingView = view.findViewById<TextView>(R.id.book_rating)
        val spiceView = view.findViewById<TextView>(R.id.book_spice)
        val coverImageView = view.findViewById<ImageView>(R.id.book_cover)
        val addToShelfButton = view.findViewById<Button>(R.id.btn_add_shelf)

        titleView.text = book.volumeInfo?.title ?: "No Title"
        authorView.text = book.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author"

        val fullDescription = book.volumeInfo?.description ?: "No Description"
        descriptionView.text = fullDescription
        descriptionView.maxLines = 4
        expandDescription.text = "Show more"

        expandDescription.setOnClickListener {
            isDescriptionExpanded = !isDescriptionExpanded
            if (isDescriptionExpanded) {
                descriptionView.maxLines = Integer.MAX_VALUE
                expandDescription.text = "Show less"
            } else {
                descriptionView.maxLines = 4
                expandDescription.text = "Show more"
            }
        }

        val isbn10 = book.volumeInfo?.industryIdentifiers
            ?.find { it.type == "ISBN_10" }
            ?.identifier ?: "N/A"
        isbnView.text = "ISBN-10: $isbn10"

        ratingView.text = "User Rating: coming soon"
        spiceView.text = "Spice Rating: coming soon"

        val imageUrl = book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
        if (imageUrl != null) {
            coverImageView.load(imageUrl)
        } else {
            coverImageView.setImageResource(R.drawable.placeholder_image)
        }

        addToShelfButton.setOnClickListener {
            val shelves = ShelfManager.getShelves()
            val shelfNames = shelves.map { it.name }.toTypedArray()

            AlertDialog.Builder(requireContext())
                .setTitle("Select a Shelf")
                .setItems(shelfNames) { _, which ->
                    val selectedShelf = shelves[which]
                    ShelfManager.addBookToShelf(book, selectedShelf.id)
                    Toast.makeText(context, "Added to ${selectedShelf.name}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}