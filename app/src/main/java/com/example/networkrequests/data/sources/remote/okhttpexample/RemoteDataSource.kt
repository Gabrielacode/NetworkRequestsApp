package com.example.networkrequests.data.sources.remote.okhttpexample

import com.example.networkrequests.data.sources.remote.Result
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request


suspend inline fun <reified T> makeRequest(client:OkHttpClient, request: Request, ): Result {
          val response =   try {
              client.newCall(request).execute()


          }catch (e:Exception){
              return Result.Failure.NetworkFailure(e)
          }

          if(response.isSuccessful){
              return Result.Success(Json.decodeFromString<T>(response.body?.string()!!))
          }else{

              return Result.Failure.ApiFailure(UnsupportedOperationException(), response.message)
          }
       }
