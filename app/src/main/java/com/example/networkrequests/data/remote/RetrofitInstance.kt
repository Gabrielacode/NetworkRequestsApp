package com.example.networkrequests.data.remote

import com.example.networkrequests.data.HttpRoutes
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitInstance {
    private val json = Json { ignoreUnknownKeys = true } //Here we ignore any unknown properties instead of throwing a serialization exception


    //We then set our Json Converter Factory
   private val converterFactor =  json.asConverterFactory("application/json".toMediaType())
    //Here we create our interceptor
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    //Then our custom OkHttp Client
    private  val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val retrofitInstace =
        Retrofit.Builder()
            .addConverterFactory(converterFactor)
            .baseUrl(HttpRoutes.BASE_URL)
            .client(client)
            .build()

    val productApiService = retrofitInstace.create(DummyJsonApi::class.java)
}