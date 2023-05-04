package com.example.projekt1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.model.Movie

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
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

}