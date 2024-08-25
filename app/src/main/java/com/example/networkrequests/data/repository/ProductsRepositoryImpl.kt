package com.example.networkrequests.data.repository

import com.example.networkrequests.data.Result
import com.example.networkrequests.data.models.Product
import com.example.networkrequests.data.models.ProductApiResult
import com.example.networkrequests.data.remote.DummyJsonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okio.IOException
import retrofit2.HttpException

class ProductsRepositoryImpl( private val apiService :DummyJsonApi) :ProductsRepository {
    override suspend fun getAllProducts(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success<List<Product>>(apiService.getProductsList().products)
            } catch (e: IOException) {
                Result.Failure.NetworkFailure(e)
            } catch (e: HttpException) {
                Result.Failure.ApiFailure(e.message())
            }

        }
    }
}