package com.example.networkrequests.data.sources.remote.services

import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.data.sources.remote.networkmodels.ProductApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface DummyJsonApi {
    @GET("products") //Here in the Http Method Annotation we specify the Path either statically or dynamically
    suspend fun getProductsList(): ProductApiResult

    @GET("products/{id}")
    suspend fun getProductById( @Path("id")id :Int): Product

    @GET("products/")
    suspend fun getProductsByPagination(@Query("limit") limit:Int, @Query("skip") skip:Int): ProductApiResult
    @GET("products/")
    suspend fun getProductsByPaginationSortedAndOrdered(@Query("limit") limit:Int, @Query("skip") skip:Int, @Query("sortBy") query:String,@Query("order") order: String):ProductApiResult
    //Here we set limit to 10 and skip to 0 at the begining

    //Today we working with caching amd adding interceptors  in OkHttp
    //I have seen many ppl use the database style but i feel it is unsafe
    //It is better for the normal okhttp caching as if we need revalidation of the cache





}
//We always need to create our result class for either error or success
enum class SortOrder(val order:String){
    ASC("asc"),
    DESC("desc")
}