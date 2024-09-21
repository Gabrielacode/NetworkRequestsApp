package com.example.networkrequests.di.modules

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.ui.MainActivity
import com.example.networkrequests.ui.utils.NetworkUtils
import com.example.networkrequests.ui.viewmodels.ActivityViewModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //Since Hilt creates the components for us we have to use this annotation to install the module into the component
//Here we want to install it into the Application component which is normally of type SingletonComponent
class AppModule{


    @Provides
    @Singleton //We want only one retrofit instance
    fun providesRetrofit(@ApplicationContext context: Context, networkUtils:NetworkUtils): Retrofit =
        RetrofitInstance.initializeRetrofit(context,networkUtils)

    @Provides
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @Provides
    fun providesDummyJsonApi( retrofit: Retrofit): DummyJsonApi {
        return retrofit.create(DummyJsonApi::class.java)
    }

    @Provides
    @Singleton
    //@Named("skibidi")This is  qualifier
    fun provideSimpleDummyDatabase(@ApplicationContext context: Context): SimpleDummyDatabase {
        return SimpleDummyDatabase.getInstance(context)
    }
    //We then provide bindings for Retrofit  and Network Utils
}

@Module
@InstallIn(ViewModelComponent::class) //This will be for view model
class ViewModelModule{


}