package com.example.networkrequests.data.repository

import com.example.networkrequests.data.sources.remote.Result

//Just for abstraction sake as we know to avoid tight coupling
interface ProductsRepository {
    suspend fun getAllProducts(): Result
    suspend fun getProductById(id:Int): Result
    suspend fun  getProductsPaged(limit:Int,skip:Int): Result
}