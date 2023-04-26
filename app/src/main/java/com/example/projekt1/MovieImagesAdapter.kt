package com.example.projekt1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.databinding.MovieImageBinding

class MovieImageViewHolder(val binding: MovieImageBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(movId: Int, isSelected: Boolean) {
        binding.coverImage.setImageResource(movId)
        binding.selectedImage.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class MovieImagesAdapter : RecyclerView.Adapter<MovieImageViewHolder>() {
    private val coverImages = listOf(
        R.drawable.flash_ver6,
        R.drawable.john_wick_chapter_four_ver2,
        R.drawable.super_mario_bros_the_movie_ver10,
        R.drawable.renfield,
        R.drawable.turning_red
    )
    private var selectedPosition: Int = 0
        get() = coverImages[selectedPosition]

    var selectedIdMov: Int = coverImages[0]
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieImageViewHolder {
        val binding = MovieImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieImageViewHolder, position: Int) {
        holder.bind(coverImages[position], coverImages[position] == selectedPosition)
    }

    override fun getItemCount(): Int = coverImages.size


}