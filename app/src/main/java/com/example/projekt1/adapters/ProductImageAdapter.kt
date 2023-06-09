package com.example.projekt1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.MovieImageBinding

class ProductImageViewHolder(private val binding: MovieImageBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(movId: Int, isSelected: Boolean) {
        binding.image.setImageResource(movId)
        binding.selectedImage.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class ProductImageAdapter : RecyclerView.Adapter<ProductImageViewHolder>() {
    private val coverImages = listOf(
        R.drawable.flash_ver6,
        R.drawable.john_wick_chapter_four_ver2,
        R.drawable.super_mario_bros_the_movie_ver10,
        R.drawable.renfield,
        R.drawable.turning_red
    )
    private var selectedPosition: Int = 0
    val selectedIdMov: Int
        get() = coverImages[selectedPosition]


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = MovieImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                setSelected(vh.layoutPosition)
            }
        }
    }

    private fun setSelected(layoutPosition: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = layoutPosition
        notifyItemChanged(selectedPosition)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(coverImages[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = coverImages.size
    fun setSelection(cover: Int?) {
        val index = coverImages.indexOfFirst { it == cover }
        if (index == -1) {
            return
        } else {
            setSelected(index)
        }

    }
}