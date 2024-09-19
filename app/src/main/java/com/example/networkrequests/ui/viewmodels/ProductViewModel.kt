package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networkrequests.data.sources.remote.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProductViewModel( private val productRepo:com.example.networkrequests.data.repository.ProductsRepository) : ViewModel() {
       private val _currentProductsListUiState:MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState.isLoading())
        val currentProductsListUiState:StateFlow<ProductUiState> = _currentProductsListUiState.asStateFlow()

   fun getProducts(){
       _currentProductsListUiState.value = ProductUiState.isLoading()
           viewModelScope.launch {
                val result =productRepo.getAllProducts()
                _currentProductsListUiState.value =  when(result){
                    is Result.Success<*> ->{
                         ProductUiState.onSuccess(result.result)
                    }
                    is Result.Failure.ApiFailure -> {
                         ProductUiState.OnError(result.errorMessage)
                    }
                    is Result.Failure.NetworkFailure -> {
                         ProductUiState.OnError("Error Message")
                    }
                }
           }

   }   }
class ProductViewModelFactory(private val productRepo:com.example.networkrequests.data.repository.ProductsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductViewModel::class.java)) ProductViewModel(productRepo) as T
        else super.create(modelClass)
    }
}
