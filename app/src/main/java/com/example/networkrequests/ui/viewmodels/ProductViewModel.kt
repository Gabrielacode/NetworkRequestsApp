package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networkrequests.data.HttpClient
import com.example.networkrequests.data.ProductsRepository
import com.example.networkrequests.data.Result
import com.example.networkrequests.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProductViewModel( private val productRepo:com.example.networkrequests.data.repository.ProductsRepository) : ViewModel() {
       private val _currentProductsListUiState:MutableStateFlow<ProductsListUiState> = MutableStateFlow(ProductsListUiState.isLoading())
        val currentProductsListUiState:StateFlow<ProductsListUiState> = _currentProductsListUiState.asStateFlow()

   fun getProducts(){
       _currentProductsListUiState.value = ProductsListUiState.isLoading()
           viewModelScope.launch {
                val result =productRepo.getAllProducts()
                _currentProductsListUiState.value =  when(result){
                    is Result.Success<*> ->{
                         ProductsListUiState.onSuccess(result.result)
                    }
                    is Result.Failure.ApiFailure -> {
                         ProductsListUiState.OnError(result.errorMessage)
                    }
                    is Result.Failure.NetworkFailure -> {
                         ProductsListUiState.OnError("Error Message")
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
sealed interface ProductsListUiState{
    class isLoading():ProductsListUiState
    class onSuccess<T>(val data :T):ProductsListUiState
    class OnError(val message:String):ProductsListUiState
}