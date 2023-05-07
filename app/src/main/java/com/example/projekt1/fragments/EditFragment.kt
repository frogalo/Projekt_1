package com.example.projekt1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val newMovie = MovieEntity(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                cover = resources.getResourceEntryName(adapter.selectedIdMov),
                rating = binding.rating.text.toString().toDoubleOrNull() ?: 0.0
            )

            thread {
                MovieDatabase.open(requireContext()).movies.addMovie(newMovie)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
            }
        }
    }


}