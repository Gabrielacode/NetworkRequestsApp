package com.example.networkrequests.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.networkrequests.R
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.data.repository.ProductsRepositoryImpl
import com.example.networkrequests.databinding.ProductItemPageBinding
import com.example.networkrequests.ui.viewmodels.ProductItemViewModel
import com.example.networkrequests.ui.viewmodels.ProductItemViewModelFactory
import com.example.networkrequests.ui.viewmodels.ProductUiState
import kotlinx.coroutines.launch

class ProductItemFragment: Fragment() {
    lateinit var binding: ProductItemPageBinding
    val viewModel: ProductItemViewModel by viewModels<ProductItemViewModel> {
        ProductItemViewModelFactory(
            ProductsRepositoryImpl(RetrofitInstance.productApiService!!)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductItemPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("Id")
        if (id == null) {
            Toast.makeText(requireContext(), "InvalidProductId", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentProductsListUiState.collect {
                when (it) {
                    is ProductUiState.OnError -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    is ProductUiState.isLoading -> {


                    }

                    is ProductUiState.onSuccess<*> -> {
                        val product = it.data as Product
                        bindProductData(product)
                    }
                }
            }


        }
        viewModel.getProductById(id!!)

    }

    fun bindProductData(product: Product){
        binding.titleTv.text = product.title
        binding.descriptionTv.text = product.description
        Glide.with(requireContext()).load(product.thumbnail).placeholder(R.drawable.ic_launcher_background
        ).into(binding.thumbNail)
    }
}