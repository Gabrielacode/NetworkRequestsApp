package com.example.networkrequests.data

import com.example.networkrequests.data.models.ProductApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class ProductsRepository {

    suspend fun getAllProducts(client:OkHttpClient) : Result=
        withContext(Dispatchers.IO){
            val url = "${HttpRoutes.BASE_URL}/${HttpRoutes.PRODUCTS_DIR}"
            val request = Request.Builder()
                .url(url)
                .build()
             makeRequest<ProductApiResult>(client,request)

        }

}
