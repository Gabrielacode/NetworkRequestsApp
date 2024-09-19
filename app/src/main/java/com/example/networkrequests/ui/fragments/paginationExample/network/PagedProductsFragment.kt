package com.example.networkrequests.ui.fragments.paginationExample.network

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkrequests.data.pagingexample.ProductPagingSource
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.data.repository.PagedProductsRepository
import com.example.networkrequests.data.repository.ProductsRepositoryImpl
import com.example.networkrequests.databinding.ProductLayoutBinding
import com.example.networkrequests.ui.adapters.PagedProductListAdapter
import com.example.networkrequests.ui.viewmodels.PagedProductViewModel
import com.example.networkrequests.ui.viewmodels.PagedProductViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagedProductsFragment: Fragment() {
//I know see why dependency injection is good
val viewModel by viewModels<PagedProductViewModel> { PagedProductViewModelFactory(PagedProductsRepository(
    ProductPagingSource(ProductsRepositoryImpl(RetrofitInstance.productApiService!!)))) }
    lateinit var binding: ProductLayoutBinding
    val pagedadapter = PagedProductListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductLayoutBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //We know pass theadapter to the recycler view
        binding.productsList.apply {
            this.adapter = pagedadapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flowOfPagingData.collectLatest {
                   Log.i("PagingData",it.toString())

                   pagedadapter.submitData(it)

                //pagedadapter.refresh() //This is one way we can refresh data  meaning invalidating the paging source  meaning we refresh the paging data since they come in a pair
                //Paging data just contians the data of pages a paging source has loaded note !
                //Yay if we then combine it with network caching we can use less bandwidth
            }
        }


    }
}