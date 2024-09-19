package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.cachedIn
import com.example.networkrequests.data.repository.PagedProductsRepository
import kotlinx.coroutines.flow.forEach

class PagedProductViewModel( val pagedProductRepository:PagedProductsRepository) :ViewModel() {
    val flowOfPagingData = pagedProductRepository.getPagedListOfProducts().cachedIn(viewModelScope)
    //The cached in ensures that on configuration changes or process death, the new activity will recieve the existing data instead of fetching new data from scratch
    //Which will take more time

}
class PagedProductViewModelFactory(val pagedProductRepository: PagedProductsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if(modelClass.isAssignableFrom(PagedProductViewModel::class.java)) PagedProductViewModel(pagedProductRepository) as T
        else super.create(modelClass, extras)
    }
}