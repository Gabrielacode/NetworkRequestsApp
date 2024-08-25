package com.example.networkrequests.data.remote

import com.example.networkrequests.data.models.ProductApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyJsonApi {
    @GET("products") //Here in the Http Method Annotation we specify the Path either statically or dynamically
    suspend fun getProductsList(): ProductApiResult



}
//We always need to create our result class for either error or success