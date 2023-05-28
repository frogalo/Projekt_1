import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.data.Product
import com.example.projekt1.ProductCallback
import com.example.projekt1.data.ProductDao
import com.example.projekt1.data.ProductDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(product: Product) {
        binding.name.text = product.name
        binding.description.text = product.description
//        binding.image.setImageResource(product.prodId)
        binding.price.text = product.price.toString() + " PLN"
    }
}

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>() {
    private val data = mutableListOf<Product>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private lateinit var productDao: ProductDao
    var onItemClick: (Int) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        productDao = ProductDatabase.open(binding.root.context).products
        val viewHolder = ProductViewHolder(binding)
        binding.root.setOnLongClickListener { view ->
            val position = viewHolder.adapterPosition
            val item = data[position]
            showConfirmationDialog(view, item)
            true
        }
        binding.btnEdit.setOnClickListener {
            onItemClick(data[viewHolder.layoutPosition].id)
        }
        return viewHolder
    }

    private fun showConfirmationDialog(view: View, product: Product) {
        val message = "Do you want to remove this item?"
        AlertDialog.Builder(view.context)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                removeItem(product)
                refresh()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    private fun removeItem(product: Product) {
        val position = data.indexOf(product)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
            GlobalScope.launch {
                productDao.removeProduct(product.id)
            }
        }
        refresh()
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
    interface AdapterCallback {
        fun onRefresh()
    }
    private var adapterCallback: AdapterCallback? = null
    fun setAdapterCallback(callback: AdapterCallback) {
        adapterCallback = callback
    }
    fun refresh() {
        data.clear()
        notifyDataSetChanged()
        adapterCallback?.onRefresh()
    }
}


