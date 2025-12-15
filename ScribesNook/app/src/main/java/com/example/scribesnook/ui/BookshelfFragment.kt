package com.example.scribesnook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.scribesnook.R
import com.example.scribesnook.data.ShelfManager
import com.example.scribesnook.model.ShelfBook
import kotlinx.coroutines.launch

class BookshelfFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShelfAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bookshelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_shelves)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ PASS SHELVES — NOT ShelfBook
        adapter = ShelfAdapter(
            shelves = ShelfManager.getShelves(),
            onBookClick = { bookItem ->
                val shelfBook = ShelfBook(
                    googleId = bookItem.id,
                    title = bookItem.volumeInfo?.title,
                    authors = bookItem.volumeInfo?.authors,
                    description = bookItem.volumeInfo?.description,
                    isbn10 = bookItem.volumeInfo
                        ?.industryIdentifiers
                        ?.firstOrNull { it.type == "ISBN_10" }
                        ?.identifier,
                    thumbnailUrl = bookItem.volumeInfo?.imageLinks?.thumbnail
                )

                val action =
                    BookshelfFragmentDirections
                        .actionBookshelfToShelfBookDetails(shelfBook)

                findNavController().navigate(action)
            }
        )

        recyclerView.adapter = adapter
    }
}