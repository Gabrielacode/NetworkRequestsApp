package com.example.networkrequests.data.repository

import com.example.networkrequests.data.sources.remote.Result
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.data.sources.remote.networkmodels.ProductApiResult
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

class ProductsRepositoryImpl( private val apiService : DummyJsonApi) :ProductsRepository {
    override suspend fun getAllProducts(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success<List<Product>>(apiService.getProductsList().products)
            } catch (e: IOException) {
                Result.Failure.NetworkFailure(e)
            } catch (e: HttpException) {
                Result.Failure.ApiFailure(e, e.message())
            }

        }
    }

    override suspend fun getProductById(id: Int): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success<Product>(apiService.getProductById(id))
            } catch (e: IOException) {
                Result.Failure.NetworkFailure(e)
            } catch (e: HttpException) {
                Result.Failure.ApiFailure(e,e.message())
            }

        }
    }

    override suspend fun getProductsPaged(limit: Int, skip: Int): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success<ProductApiResult>(apiService.getProductsByPagination(limit,skip))
            } catch (e: IOException) {
                Result.Failure.NetworkFailure(e)
            } catch (e: HttpException) {
                Result.Failure.ApiFailure(e,e.message())
            }

        }
    }
}