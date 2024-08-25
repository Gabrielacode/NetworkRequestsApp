package com.example.networkrequests.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.networkrequests.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
//In this project we will work with the network  on android
/*
Since we have learnt how to make network requests with OkHttp
We will learn how to manage networkin our app and also  connection ,and giving users control over
We will use the NETWORK and MANAGE NETWORK STATE permissions for that
At the base Android uses HttpsUrlConnection
We then okhttp . Retrofit , and also Ktor
We should not make network operations on the main thread 
* */