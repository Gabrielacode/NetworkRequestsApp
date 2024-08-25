package com.example.networkrequests.data.repository

import com.example.networkrequests.data.Result
import com.example.networkrequests.data.models.ProductApiResult

//Just for abstraction sake as we know to avoid tight coupling
interface ProductsRepository {
    suspend fun getAllProducts():Result
}