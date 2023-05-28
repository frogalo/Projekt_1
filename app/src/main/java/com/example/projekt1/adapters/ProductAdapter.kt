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
import com.example.projekt1.data.Product
import com.example.projekt1.ProductCallback
import com.example.projekt1.data.ProductDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        binding.name.text = product.name
        binding.description.text = product.description
        binding.image.setImageResource(product.prodId)
        binding.price.text = product.price.toString()
    }
    private val productDao = ProductDatabase.open(binding.root.context).products

}

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>() {
    private val data = mutableListOf<Product>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var onItemClick: (Int) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        var productDao = ProductDatabase.open(binding.root.context).products
        val viewHolder = ProductViewHolder(binding)
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
                            productDao.removeProduct(item.id)
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

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun replace(newData: List<Product>) {
        val callback = ProductCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

    fun sort() {
        val notSorted = data.toList()
        data.sortBy { it.price }
        val callback = ProductCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

}