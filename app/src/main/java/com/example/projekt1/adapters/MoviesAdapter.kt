package com.example.projekt1.adapters

import android.app.AlertDialog
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
import com.example.projekt1.data.MovieDao
import com.example.projekt1.data.MovieDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {
        binding.title.text = movie.title
        binding.description.text = movie.description
        binding.coverImage.setImageResource(movie.movId)
        binding.rating.text = movie.rating.toString()
    }
    private val movieDao = MovieDatabase.open(binding.root.context).movies

}

class MoviesAdapter : RecyclerView.Adapter<MovieViewHolder>() {
    private val data = mutableListOf<Movie>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var onItemClick: (Int) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        var movieDao = MovieDatabase.open(binding.root.context).movies // Initialize movieDao
        val viewHolder = MovieViewHolder(binding)
        binding.root.setOnLongClickListener { view ->
            var dialog: AlertDialog? = null
            handler.postDelayed({
                // Show confirmation dialog
                val message = "Do you want to remove this item?"
                dialog = AlertDialog.Builder(view.context)
                    .setMessage(message)
                    .setPositiveButton("Yes") { _, _ ->
                        // Remove item from list and database
                        val position = viewHolder.adapterPosition
                        val item = data[position]
                        data.removeAt(position)
                        notifyItemRemoved(position)
                        // Remove item from database
                        GlobalScope.launch {
                            movieDao.removeMovie(item.id)
                        }
                        dialog?.dismiss() // Dismiss the dialog after the item has been removed
                    }
                    .setNegativeButton("No", null)
                    .create()
                dialog?.show()
            }, 2000)
            true
        }
        binding.btnEdit.setOnClickListener {
            onItemClick(data[viewHolder.layoutPosition].id)
        }
        return viewHolder
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