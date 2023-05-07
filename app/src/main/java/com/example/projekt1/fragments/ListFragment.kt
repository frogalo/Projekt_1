package com.example.projekt1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.adapters.MoviesAdapter
import com.example.projekt1.Navigable
import com.example.projekt1.data.Movie
import com.example.projekt1.data.MovieDatabase
import com.example.projekt1.databinding.FragmentListBinding
import kotlin.concurrent.thread


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: MoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoviesAdapter()
        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
            adapter?.sort()
        }
    }

    fun loadData() {
        thread {
            val movies =
                MovieDatabase.open(requireContext()).movies.getAllSortedByRating().map { entity ->
                    Movie(
                        entity.title,
                        entity.description,
                        resources.getIdentifier(
                            entity.cover,
                            "drawable",
                            requireContext().packageName
                        ),
                        entity.rating
                    )
                }

            requireActivity().runOnUiThread {
                adapter?.replace(movies)
                binding.list.adapter = adapter  //this will add loading after the app is closed and opened again
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

}