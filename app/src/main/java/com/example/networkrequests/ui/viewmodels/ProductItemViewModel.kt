package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networkrequests.data.sources.remote.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductItemViewModel(private val productRepo:com.example.networkrequests.data.repository.ProductsRepository):ViewModel() {
    private val _currentProductsListUiState: MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState.isLoading())
    val currentProductsListUiState: StateFlow<ProductUiState> = _currentProductsListUiState.asStateFlow()
fun getProductById(id:Int){

    viewModelScope.launch {
        val result = productRepo.getProductById(id)
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

}
}
class ProductItemViewModelFactory(private val productRepo:com.example.networkrequests.data.repository.ProductsRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductItemViewModel::class.java)) ProductItemViewModel(productRepo) as T
        else super.create(modelClass)
    }
}