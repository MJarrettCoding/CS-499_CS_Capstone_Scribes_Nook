package com.example.scribesnook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scribesnook.R
import com.example.scribesnook.api.BackendApi
import kotlinx.coroutines.launch

class CommunityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityBookAdapter   // ✅ FIXED TYPE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_community, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val searchInput = view.findViewById<EditText>(R.id.communitySearchInput)
        val searchButton = view.findViewById<Button>(R.id.communitySearchButton)

        recyclerView = view.findViewById(R.id.communityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CommunityBookAdapter { book ->
            val action =
                CommunityFragmentDirections
                    .actionCommunityToCommunityBookReviews(book.googleId)

            findNavController().navigate(action)
        }

        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                searchBooks(query)
            }
        }
    }

    private fun searchBooks(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = BackendApi.service.searchBooks(query)
                if (response.isSuccessful) {
                    adapter.submitList(response.body())   // ✅ BackendBook list
                } else {
                    Toast.makeText(context, "Search failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
