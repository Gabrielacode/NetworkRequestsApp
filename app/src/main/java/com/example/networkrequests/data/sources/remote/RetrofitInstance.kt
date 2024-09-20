package com.example.networkrequests.data.sources.remote

import android.content.Context
import android.util.Log
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.ui.utils.NetworkUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object RetrofitInstance {
    const val cacheFileDir = "HTTP_CACHE"

   // private var cache: Cache? = null
  //  private var client: OkHttpClient? = null
   // private var retrofitInstace: Retrofit? = null
    var productApiService: DummyJsonApi? = null


    fun setCache(context: Context):Cache {
        return Cache(File(context.cacheDir, cacheFileDir), (20 * 1024 * 1024).toLong())
    }

    fun initializeRetrofit(context: Context):Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        } //Here we ignore any unknown properties instead of throwing a serialization exception
        //We then set our Json Converter Factory
        val converterFactor =  json.asConverterFactory("application/json".toMediaType())
        //Here we create our interceptor
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        //We we create a simple Logging Interceptor for the request
        val loggingInterceptor = object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val requestLog = " Request \n URL: ${request.url} \n HEADERS:" +
                        "${request.headers}"
                Log.i("Interceptor", requestLog)

                val response = chain.proceed(request)
                val responseLog = "Response \n URL : ${response.request.url}  \n HEADERS :\n" +
                        "${response.headers}" //Note the response url
                Log.i("Interceptor", responseLog)
                Log.i("Interceptor",response.cacheResponse.toString())
                return  response
            }

        }
        val applicationloggingInterceptor = object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val requestLog = " Request \n URL: ${request.url} \n HEADERS:" +
                        "${request.headers}"
                Log.i("Interceptor", "  Application Interceptor : $requestLog")

                val response = chain.proceed(request)
                val responseLog = "Response \n URL : ${response.request.url}  \n HEADERS :\n" +
                        "${response.headers}" //Note the response url
                Log.i("Interceptor", "  Application Interceptor : $responseLog")
                Log.i("Interceptor","  Application Interceptor : ${response.cacheResponse.toString()}")
                return  response
            }

        }
        //Now lets add a cache and see whether it works



        //But the cache only works when the response contains a header containing cache-control header
        //But lets try a request and see
        //As we can see  the request didnt work
        //So lets see whether the response will make the okhttp cache it
        val cacheInterceptor = object:Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val responsewithCacheControl = chain.proceed(request).newBuilder()
                    .addHeader("Cache-Control",
                        CacheControl.Builder().maxAge(3,TimeUnit.HOURS).build().toString()
                    )
                    .build()
                return responsewithCacheControl

            }

        }
        val forceCacheInterceptor=object :Interceptor{
            @Inject
            lateinit var networkUtils: NetworkUtils
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                if(!networkUtils.hasActiveNetwork()){
                    request  =  request.newBuilder()
                        .addHeader("Cache-Control", CacheControl.FORCE_CACHE.toString())
                        .build()
                }
                return  chain.proceed(request)
            }
        }
        //Now that we can now force cache  to work when there is a cache
        //Yay
        //

    //Then our custom OkHttp Client

     val cache = setCache(context)
         val client = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(cacheInterceptor)
             .addInterceptor(forceCacheInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
             .addInterceptor(applicationloggingInterceptor)
             .authenticator(object : Authenticator {
                 override fun authenticate(route: Route?, response: Response): Request? {
                      
                     return null
                 }

             })
            .build()

     return  Retrofit.Builder()
            .addConverterFactory(converterFactor)
            .baseUrl(HttpRoutes.BASE_URL)
            .client(client)
            .build()

    }
}



//Application Interceptor works between our code and the okHttp core library
// While Network Interceptors work between the OkHttp Library and the server
/*
*
* For Example
* Request (From Code)
*      |      <- Application Interceptor
* OkHttp Library
*     |      <- Network Interceptor
* Server
*
*
*  Yaay the caching is working
* Now we need to create a case where if the user has accessed the data once  and there is no data the user can use the data if without data
* since okhttp uses the cache only when there is network
*
* Now we will look at Authentication in Http
* It is used to ensure only the allowed users can access resources from the server
* We need to supply correct credentials
* or an 401 UnAuthorized Exception
* Though the first time a user makes a request the user is anoynmous to the server
* and it sends an 401 error code
* In the response headers it also sends a header either called
* WWW-Authenticate or Proxy Authenticate which tells the client how to authenticate to the server
* There is also challenge response authentication
* where the server sends a challenge request to the client and the client must provide the required authentication procedures
* In the WWWAuthenticate header there is the type of authentication and the realm or region that the authentication is protecting
* Then when the use requests it adds the authorization header to the request
* Specifing the type and the credentials
* There are several authentication schemes
* We have
* Basic-> We send the password and user name in  as the credentials which is normally encoded in Base 64
* in each request
* Bearer->This is also known as token authentication where the server sends a unique token
* to the client on successful login
* The client also has to add this tokens to each request
* Digest -> It is a challenge response authentication
* The server sends a nonce a random number that can only once
* The client has  to send a hash which contains the username , password , the nonce , the url and the Http method
* It is more complicated that the Basic
* We also have Mutuals where both the server and client authenticate themselves
* There are many more so we can learn them later
* Lets look at when the user doesnt havce sufficent privelages
* When the user doesnt provide the correct credentials or doesnt authenticate
*Then it return A 401 Unauthorized user
*
* or a 407 Proxy Authoriztion Required if access is made through a proxy server
*
* If the user is authorized but doesnt have the suffecient privelages or permissions to access a resource
* Then a 403 Forbidden It sent
*
* If the resource is not found or sometimes the server can send a 404 Not  Found response Which can be used toshow the resources is not available or cannot be accesssed
*
* In Http we use a authenticator to authenticate
* It call s the authenticator to provide crednetials  when a 401 response code is sent
*We will use the name of the user when we login to the
* We will user one of the sample users in the okhttp
* */