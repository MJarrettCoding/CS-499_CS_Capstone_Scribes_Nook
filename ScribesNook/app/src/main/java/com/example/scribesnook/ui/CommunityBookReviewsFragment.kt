package com.example.scribesnook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scribesnook.R
import com.example.scribesnook.api.BackendApi
import kotlinx.coroutines.launch

class CommunityBookReviewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_community_book_reviews,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val googleId =
            CommunityBookReviewsFragmentArgs
                .fromBundle(requireArguments())
                .googleId

        val titleText =
            view.findViewById<TextView>(R.id.book_title)
        val avgRatingText =
            view.findViewById<TextView>(R.id.avg_rating)
        val avgSpicyText =
            view.findViewById<TextView>(R.id.avg_spicy)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.reviewsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val adapter = CommunityReviewAdapter()
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response =
                    BackendApi.service.getReviewsForBook(googleId)

                if (!response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Failed to load reviews (${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val reviews = response.body().orEmpty()

                if (reviews.isNotEmpty()) {
                    // Title comes from backend Book document
                    titleText.text = reviews.first().title ?: "Unknown Title"

                    val avgRating =
                        reviews.map { it.userRating }.average()
                    val avgSpice =
                        reviews.map { it.spicyRating }.average()

                    avgRatingText.text =
                        "⭐ Avg Rating: %.1f".format(avgRating)
                    avgSpicyText.text =
                        "🌶️ Avg Spice: %.1f".format(avgSpice)
                } else {
                    titleText.text = "No reviews yet"
                    avgRatingText.text = "⭐ Avg Rating: N/A"
                    avgSpicyText.text = "🌶️ Avg Spice: N/A"
                }

                adapter.submitList(reviews)

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Network error loading reviews",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}