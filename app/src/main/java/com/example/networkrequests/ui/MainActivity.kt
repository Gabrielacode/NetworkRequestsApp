package com.example.networkrequests.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.networkrequests.R
import com.example.networkrequests.databinding.NetworkConnectivityPopupBinding
import com.example.networkrequests.ui.utils.NetworkUtils
import com.example.networkrequests.ui.viewmodels.ActivityViewModel
import com.example.networkrequests.ui.viewmodels.ActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel :ActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this,ActivityViewModelFactory(NetworkUtils.ConnectivityObserver(applicationContext))).get(ActivityViewModel::class.java)
        viewModel.mutableConnectionStatus.observe(this){
             showPopupWindow(findViewById(R.id.main),it.toString())
        }
    }

    fun showPopupWindow( anchorview: View ,text:String){
        val binding = NetworkConnectivityPopupBinding.inflate(layoutInflater)
        binding.textView .text = text
        val view = binding.root

        val popupWindow = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true)
        popupWindow.apply {

        }
        popupWindow.showAtLocation(view,Gravity.BOTTOM,30,anchorview.height+100)

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
