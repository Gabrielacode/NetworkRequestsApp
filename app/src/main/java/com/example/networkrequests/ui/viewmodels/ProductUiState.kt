package com.example.networkrequests.ui.viewmodels

sealed interface ProductUiState{
    class isLoading():ProductUiState
    class onSuccess<T>(val data :T):ProductUiState
    class OnError(val message:String):ProductUiState
}