package com.example.networkrequests.ui.fragments.paginationExample.db

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkrequests.data.sources.database.db.PagedItemDatabase
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import com.example.networkrequests.data.repository.PagedItemRepository
import com.example.networkrequests.databinding.PagedItemLayoutBinding
import com.example.networkrequests.ui.adapters.PagedItemListAdapter
import com.example.networkrequests.ui.viewmodels.PagedItemViewModel
import com.example.networkrequests.ui.viewmodels.PagedItemViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagedItemsFragment:Fragment() {
   val viewModel : PagedItemViewModel by viewModels<PagedItemViewModel>
   { PagedItemViewModelFactory(PagedItemRepository(PagedItemDatabase.returnInstance(requireContext().applicationContext).itemDAO())) }
    lateinit var binding:PagedItemLayoutBinding
    val pagedAdapter =PagedItemListAdapter()
   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      binding = PagedItemLayoutBinding.inflate(inflater,container,false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding.apply {
         rcView.adapter = pagedAdapter
         rcView.layoutManager = LinearLayoutManager(requireContext())
         floatingActionButton.setOnClickListener {
            viewModel.insertItem(PagedItem(0, "Skibidi Toilet", "Dasjiko"))
         }
      }
      viewLifecycleOwner.lifecycleScope.launch {
         viewModel.listofPagedItems.collectLatest {
          //  Log.i("PagingData",it.toString())
            pagedAdapter.submitData(it)
         }
      }
      //The Diif utill compares the data from two paging datas to know whether to update or not just like List Adapter
   }
}