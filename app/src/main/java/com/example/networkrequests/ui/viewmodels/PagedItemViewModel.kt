package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.cachedIn
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import com.example.networkrequests.data.repository.PagedItemRepository
import kotlinx.coroutines.launch

class PagedItemViewModel(val pagedItemRepo:PagedItemRepository) : ViewModel() {
   val listofPagedItems = pagedItemRepo.getListOfItems().cachedIn(viewModelScope)

    fun insertItem(item: PagedItem){
        viewModelScope.launch {
            pagedItemRepo.insertItem(item)
        }
    }
    fun deleteItem(item: PagedItem){
        viewModelScope.launch {
            pagedItemRepo.deleteItem(item)
        }
    }

}
class PagedItemViewModelFactory(val pagedItemRepo:PagedItemRepository) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if(modelClass.isAssignableFrom(PagedItemViewModel::class.java)) PagedItemViewModel(pagedItemRepo) as T
        else super.create(modelClass,extras)
    }
}