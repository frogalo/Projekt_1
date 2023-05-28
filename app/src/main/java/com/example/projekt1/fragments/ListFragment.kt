package com.example.projekt1.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.adapters.ProductAdapter
import com.example.projekt1.Navigable
import com.example.projekt1.R
import com.example.projekt1.data.Product
import com.example.projekt1.data.ProductDatabase
import com.example.projekt1.databinding.FragmentListBinding
import java.util.Locale
import kotlin.concurrent.thread


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: ProductAdapter? = null
    private var isPolishLanguage: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductAdapter().apply {
            onItemClick = {
                (activity as? Navigable)?.navigate(Navigable.Destination.Edit, it)
            }
        }
        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }


        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
            adapter?.sort()
        }

        thread {
            val totalProducts = ProductDatabase.open(requireContext()).products.count()
            val totalSum = ProductDatabase.open(requireContext()).products.totalRating()
            requireActivity().runOnUiThread {
                binding.totalProducts.text = "$totalProducts"
                if (totalSum != null)
                    binding.totalSum.text = "%.1f".format(totalSum)
                else
                    binding.totalSum.text = "0"
            }
        }
    }

    private fun swapLanguage() {
        isPolishLanguage = !isPolishLanguage
        val locale = if (isPolishLanguage) {
            Locale("pl")
        } else {
            Locale("en")
        }

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        // Reload the activity to reflect the new language
        requireActivity().recreate()
    }

    private fun updateTranslations() {
        val context = requireContext()
        val resources = context.resources
        binding.btAdd.text = resources.getString(R.string.add)
        binding.totalProductsLabel.text = resources.getString(R.string.products)
        binding.totalSumLabel.text = resources.getString(R.string.totalSum)
        // Update any other views that require translation updates
    }

    fun loadData() {
        thread {
            val products =
                ProductDatabase.open(requireContext()).products.getAllSortedByRating()
                    .map { entity ->
                        Product(
                            entity.prodId,
                            entity.name,
                            entity.description,
                            resources.getIdentifier(
                                entity.image,
                                "drawable",
                                requireContext().packageName
                            ),
                            entity.price
                        )
                    }

            requireActivity().runOnUiThread {
                adapter?.replace(products)
                binding.list.adapter =
                    adapter  //this will add loading after the app is closed and opened again
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

}