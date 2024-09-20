package com.example.networkrequests.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.ui.fragments.paginationExample.both.BothRemoteAndCacheFragmentComponent
import com.example.networkrequests.ui.utils.NetworkUtils
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Component(modules = [AppModule::class,SubModule::class])
interface ApplicationComponent{
//This will be the main component

    fun mainActivityBuilder():MainActivityComponent.Builder
    fun bothCacheAndRemoteBuilder():BothRemoteAndCacheFragmentComponent.Builder

    //We then create a builder
    @Component.Builder
    interface Builder{
        //Here we use BindsInstance to pass the Context through the dependency graph
        @BindsInstance
        fun passApplicationContext(context: Context):Builder
         fun build():ApplicationComponent

    }
}
//Now lets create a module
@Module
 class AppModule{


     @Provides
     @Singleton //We want only one retrofit instance
     fun providesRetrofit( context:Context):Retrofit =
         RetrofitInstance.initializeRetrofit(context)

    @Provides
     fun providesConnectivityManager(context: Context):ConnectivityManager{
         return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
     }


    @Provides
    fun providesDummyJsonApi(retrofit: Retrofit):DummyJsonApi{
        return retrofit.create(DummyJsonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSimpleDummyDatabase(context: Context):SimpleDummyDatabase{
        return SimpleDummyDatabase.getInstance(context)
    }
     //We then provide bindings for Retrofit  and Network Utils
 }

//Module for our sub components
@Module(subcomponents = [MainActivityComponent::class,BothRemoteAndCacheFragmentComponent::class])
interface SubModule
//Now lets move to activity

class MainApp : Application() {
    lateinit var appComponent:ApplicationComponent
    override fun onCreate() {
        super.onCreate()
          //Now lets us create the component
     appComponent = DaggerApplicationComponent.builder().passApplicationContext(applicationContext).build()

    }
}