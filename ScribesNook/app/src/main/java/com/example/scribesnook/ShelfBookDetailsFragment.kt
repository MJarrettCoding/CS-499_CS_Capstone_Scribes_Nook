package com.example.scribesnook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.scribesnook.Prefs
import com.example.scribesnook.R
import com.example.scribesnook.SaveRatingRequest
import com.example.scribesnook.api.BackendApi

import com.example.scribesnook.model.ShelfBook
import kotlinx.coroutines.launch

class ShelfBookDetailsFragment : Fragment() {

    private lateinit var shelfBook: ShelfBook

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_shelf_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shelfBook =
            ShelfBookDetailsFragmentArgs.fromBundle(requireArguments()).shelfBook

        val coverImage = view.findViewById<ImageView>(R.id.book_cover)
        val titleText = view.findViewById<TextView>(R.id.book_title)
        val authorText = view.findViewById<TextView>(R.id.book_authors)
        val isbnText = view.findViewById<TextView>(R.id.book_isbn)
        val descriptionText = view.findViewById<TextView>(R.id.book_description)

        val userRatingBar = view.findViewById<RatingBar>(R.id.user_rating)
        val spicyRatingBar = view.findViewById<RatingBar>(R.id.spicy_rating)
        val reviewEditText = view.findViewById<EditText>(R.id.review_text)
        val submitButton = view.findViewById<Button>(R.id.submit_rating_review_button)

        titleText.text = shelfBook.title ?: "No Title"
        authorText.text = shelfBook.authors?.joinToString(", ") ?: "Unknown Author"
        isbnText.text = "ISBN-10: ${shelfBook.isbn10 ?: "N/A"}"
        descriptionText.text = shelfBook.description ?: "No Description"

        if (!shelfBook.thumbnailUrl.isNullOrEmpty()) {
            coverImage.load(shelfBook.thumbnailUrl!!.replace("http://", "https://"))
        } else {
            coverImage.setImageResource(R.drawable.placeholder_image)
        }

        submitButton.setOnClickListener {
            submitRating(
                userRatingBar.rating,
                spicyRatingBar.rating,
                reviewEditText.text.toString()
            )
        }
    }

    private fun submitRating(
        userRating: Float,
        spicyRating: Float,
        reviewText: String
    ) {
        val token = Prefs.getToken(requireContext())

        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = BackendApi.service.saveRating(
                    authHeader = "Bearer $token",
                    body = SaveRatingRequest(
                        googleId = shelfBook.googleId,
                        title = shelfBook.title,
                        authors = shelfBook.authors,
                        description = shelfBook.description,
                        isbn10 = shelfBook.isbn10,
                        thumbnailUrl = shelfBook.thumbnailUrl,
                        userRating = userRating,
                        spicyRating = spicyRating,
                        reviewText = reviewText
                    )
                )

                if (response.isSuccessful) {
                    Toast.makeText(context, "Review saved!", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("SAVE_REVIEW", error ?: "Unknown error")
                    Toast.makeText(
                        context,
                        "Failed to save review (${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("SAVE_REVIEW", "Network error", e)
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


