package com.example.networkrequests.data

import kotlin.Result

sealed interface Result {
     class Success<T>( val result:T):com.example.networkrequests.data.Result
      sealed interface Failure :com.example.networkrequests.data.Result{
         class ApiFailure( val errorMessage:String):Failure
         class NetworkFailure(e :Exception):Failure
     }
}
//NetworkFailure Exceptions are IO exception
//and Illegal State Exception