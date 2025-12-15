package com.example.scribesnook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scribesnook.BookAdapter
import com.example.scribesnook.BookResponse
import com.example.scribesnook.GoogleBooksApi
import com.example.scribesnook.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchInput = view.findViewById(R.id.search_input)
        recyclerView = view.findViewById(R.id.book_recycler)

        // ✅ SINGLE adapter, Safe Args navigation
        bookAdapter = BookAdapter { book ->
            val action =
                SearchFragmentDirections.actionSearchToBookDetails(googleBook = book)
            findNavController().navigate(action)
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = bookAdapter

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    fetchBooks(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun fetchBooks(query: String) {
        GoogleBooksApi.service.searchBooks(query, maxResults = 40)
            .enqueue(object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    if (response.isSuccessful) {
                        val books = response.body()?.items ?: emptyList()
                        bookAdapter.submitList(books)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No results found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch books",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}