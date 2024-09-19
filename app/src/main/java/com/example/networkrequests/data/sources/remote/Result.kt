package com.example.networkrequests.data.sources.remote

sealed interface Result {
     class Success<T>( val result:T): com.example.networkrequests.data.sources.remote.Result
      sealed class Failure(val e:Exception) :
          com.example.networkrequests.data.sources.remote.Result {
         class ApiFailure( error :Exception, val errorMessage:String): Failure(error)
         class NetworkFailure(  error :Exception): Failure(error)
     }
}
//NetworkFailure Exceptions are IO exception
//and Illegal State Exception