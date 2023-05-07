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
            val newMovie = Movie(
                binding.title.text.toString(),
                binding.description.text.toString(),
                adapter.selectedIdMov,
                binding.rating.text.toString().toDoubleOrNull() ?: 0.0
            )
            thread {
                DataSource.movies.add(newMovie)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
            }
        }
    }


}