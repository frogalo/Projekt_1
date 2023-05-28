package com.example.projekt1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.adapters.MovieImagesAdapter
import com.example.projekt1.Navigable
import com.example.projekt1.databinding.FragmentEditBinding
import com.example.projekt1.data.ProductDatabase
import com.example.projekt1.data.model.ProductEntity
import kotlin.concurrent.thread

const val ARG_EDIT_IT = "edit_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: MovieImagesAdapter
    private lateinit var db: ProductDatabase
    private var product: ProductEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())

    }

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
        if (arguments != null && requireArguments().containsKey(ARG_EDIT_IT)) {
            val id = requireArguments().getInt(ARG_EDIT_IT, -1)

            if (id != -1) {
                thread {
                    product = db.products.getMovie(id)
                    requireActivity().runOnUiThread {
                        binding.name.setText(product?.name ?: "")
                        binding.description.setText(product?.description ?: "")
                        binding.price.setText(product?.price.toString())


                        adapter.setSelection(product?.image?.let {
                            resources.getIdentifier(
                                it,
                                "drawable",
                                requireContext().packageName
                            )
                        })
                    }
                }
            }
        }



        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.btnSave.setOnClickListener {

            val name = binding.name.text.toString()
            val description = binding.description.text.toString()
            val price = binding.price.text.toString().toDoubleOrNull() ?: 0.0
            val image = resources.getResourceEntryName(adapter.selectedIdMov)
            val product = product?.copy(
                name = name,
                description = description,
                image = image,
                price = price
            ) ?: ProductEntity(
                name = name,
                description = description,
                image = image,
                price = price
            )
            this.product = product
            if (name.isNotEmpty() && description.isNotEmpty()) {
                thread {
                    db.products.addMovie(product)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}