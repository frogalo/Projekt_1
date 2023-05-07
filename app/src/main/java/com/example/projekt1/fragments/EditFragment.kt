package com.example.projekt1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.MovieImagesAdapter
import com.example.projekt1.Navigable
import com.example.projekt1.databinding.FragmentEditBinding
import com.example.projekt1.data.DataSource
import com.example.projekt1.data.Movie
import com.example.projekt1.data.MovieDatabase
import com.example.projekt1.data.model.MovieEntity
import kotlin.concurrent.thread


class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding

    private lateinit var adapter: MovieImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MovieImagesAdapter()
        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.btnSave.setOnClickListener {

            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val rating = binding.rating.text.toString().toDoubleOrNull() ?: 0.0

            val newMovie = MovieEntity(
                title = title,
                description = description,
                cover = resources.getResourceEntryName(adapter.selectedIdMov),
                rating = rating
            )
            if (rating > 10.0) {
                Toast.makeText(
                    requireContext(),
                    "Rating cannot be higher than 10",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (title.isNotEmpty() && description.isNotEmpty()) {
                thread {
                    MovieDatabase.open(requireContext()).movies.addMovie(newMovie)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}