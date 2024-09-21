package com.example.networkrequests.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.ui.utils.NetworkUtils
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import javax.inject.Singleton


//@Component(modules = [AppModule::class,SubModule::class])
//interface ApplicationComponent{
////This will be the main component
//
//    fun mainActivityBuilder():MainActivityComponent.Builder
//    fun bothCacheAndRemoteBuilder():BothRemoteAndCacheFragmentComponent.Builder
//
//    //We then create a builder
//    @Component.Builder
//    interface Builder{
//        //Here we use BindsInstance to pass the Context through the dependency graph
//        @BindsInstance
//        fun passApplicationContext(context: Context):Builder
//         fun build():ApplicationComponent
//
//    }
//}
//Now lets create a module


//Module for our sub components
//@Module(subcomponents = [MainActivityComponent::class,BothRemoteAndCacheFragmentComponent::class])
//interface SubModule
////Now lets move to activity
@HiltAndroidApp//This tells Hilt that this is the application class and we need to start generating components from here This is where the application context is gotten from

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
          //Now lets us create the component


    }
}