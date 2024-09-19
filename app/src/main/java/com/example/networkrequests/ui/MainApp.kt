package com.example.networkrequests.ui

import android.app.Application
import com.example.networkrequests.data.sources.remote.RetrofitInstance
import com.example.networkrequests.ui.utils.NetworkUtils

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initializeRetrofit(applicationContext)
        NetworkUtils.initManager(applicationContext)
    }
}