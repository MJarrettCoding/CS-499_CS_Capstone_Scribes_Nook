package com.example.scribesnook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scribesnook.R
import com.example.scribesnook.api.CommunityReview   // ✅ FIXED IMPORT

class CommunityReviewAdapter :
    ListAdapter<CommunityReview, CommunityReviewAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ratingText: TextView = itemView.findViewById(R.id.review_rating)
        private val spicyText: TextView = itemView.findViewById(R.id.review_spicy)
        private val reviewText: TextView = itemView.findViewById(R.id.review_text)

        fun bind(review: CommunityReview) {
            ratingText.text = "⭐ ${review.userRating}"
            spicyText.text = "🌶️ ${review.spicyRating}"
            reviewText.text = review.reviewText
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CommunityReview>() {
        override fun areItemsTheSame(oldItem: CommunityReview, newItem: CommunityReview): Boolean =
            oldItem.reviewText == newItem.reviewText

        override fun areContentsTheSame(oldItem: CommunityReview, newItem: CommunityReview): Boolean =
            oldItem == newItem
    }
}

