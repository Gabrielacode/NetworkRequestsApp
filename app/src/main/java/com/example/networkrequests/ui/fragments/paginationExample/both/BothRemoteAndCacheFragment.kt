package com.example.networkrequests.ui.fragments.paginationExample.both

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkrequests.data.repository.PagedProductsFromNetworkAndCache
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.databinding.DropdownBoxBinding
import com.example.networkrequests.databinding.ProductLayoutBinding
import com.example.networkrequests.ui.adapters.BothCacheAndNetworkAdapter
import com.example.networkrequests.ui.adapters.PagedLoadStateAdapter
import com.example.networkrequests.ui.adapters.PagedProductListAdapter
import com.example.networkrequests.ui.viewmodels.PagedNetworkAndCacheViewModel
import com.example.networkrequests.ui.viewmodels.PagedNetworkAndCacheViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BothRemoteAndCacheFragment:Fragment() {
    val viewModel :PagedNetworkAndCacheViewModel by viewModels<PagedNetworkAndCacheViewModel>
    { PagedNetworkAndCacheViewModelFactory(PagedProductsFromNetworkAndCache(SimpleDummyDatabase.getInstance(requireContext().applicationContext),RetrofitInstance.productApiService!!))  }
    lateinit var binding: ProductLayoutBinding
    val pagedadapter = BothCacheAndNetworkAdapter()
    var popup :PopupWindow?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.toolbar.menu.getItem(0).setOnMenuItemClickListener {
            if(popup?.isShowing == true){
                popup?.dismiss()
            }else {
                popup = createPopupWindowtoChooseOrderOfList(binding.toolbar)
            }
            return@setOnMenuItemClickListener true
        }

            viewModel.dummyItemsPagedFlow.collectFlowInLifecycle {
                Log.i("PagingData",it.toString())
                pagedadapter.submitData(it)
            }

                //pagedadapter.refresh() //This is one way we can refresh data  meaning invalidating the paging source  meaning we refresh the paging data since they come in a pair
                //Paging data just contians the data of pages a paging source has loaded note !
                //Yay if we then combine it with network caching we can use less bandwidth

        //We can access the load state either through a flow or a listener
        //There are both synchronous to the UI

        pagedadapter.loadStateFlow.collectFlowInLifecycle {
         val text =  when(it.prepend){
              is LoadState.Loading ->"Loading New Data"
             is LoadState.Error -> "Error Loading New Data"
             is LoadState.NotLoading -> "Finished Loading"
             else -> {"Error"}
         }
            Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show()

        }
        //We can also create a special adapter for the load states
        //Called Load state adapter and we can attach it as a header or Footer
        //or both
        pagedadapter.withLoadStateHeaderAndFooter(PagedLoadStateAdapter(),PagedLoadStateAdapter())
        //We can also monitor the Combined Load States which allows to to check load statesfrom the paging source or the remote mediator
        //or both but when not specified the remote mediator is chosen first in case of no value then the paging source is chosen
        //We are don for now for paging until we reach testing











        }
    fun createPopupWindowtoChooseOrderOfList( anchorView:View):PopupWindow{

        val binding =DropdownBoxBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(binding.root, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true)
        binding.titleOrder.setOnClickListener {
            pagedadapter.refresh()
            viewModel.getListOfItems("title",null).collectFlowInLifecycle { Log.i("PagingData",it.toString())
            pagedadapter.submitData(it) }
        }
        binding.categoryOrder.setOnClickListener{
            pagedadapter.refresh()
            viewModel.getListOfItems("category",null).collectFlowInLifecycle { Log.i("PagingData",it.toString())
            pagedadapter.submitData(it) }
            }
        binding.ratingOrder.setOnClickListener{
            pagedadapter.refresh()
            viewModel.getListOfItems("rating",null).collectFlowInLifecycle { Log.i("PagingData",it.toString())
            pagedadapter.submitData(it) }
           }

        popupWindow.enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        popupWindow.showAsDropDown(anchorView,0,3,Gravity.END)
        return popupWindow


    }
    fun  <T> Flow<T>.collectFlowInLifecycle(action: suspend (T)->Unit){
        val flow = this
        viewLifecycleOwner.lifecycleScope.launch {
            flow.collectLatest {
                action(it)
            }
        }
    }


    }
//Now we have learnt about adding seperators
//We are going to learn how to monitor the load states for the paged data either from the remote mediator or the paging source
// It also adds a custom adapter that we can add to show loading in the list