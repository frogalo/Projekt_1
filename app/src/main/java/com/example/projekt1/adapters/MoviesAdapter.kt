package com.example.projekt1.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.data.Movie
import com.example.projekt1.MovieCallback

class MovieViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {
        binding.title.text = movie.title
        binding.description.text = movie.description
        binding.coverImage.setImageResource(movie.movId)
        binding.rating.text = movie.rating.toString()
    }
}

class MoviesAdapter : RecyclerView.Adapter<MovieViewHolder>() {
    private val data = mutableListOf<Movie>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun replace(newData: List<Movie>) {
        val callback = MovieCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

    fun sort() {
        val notSorted = data.toList()
        data.sortBy { it.rating }
        val callback = MovieCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

}