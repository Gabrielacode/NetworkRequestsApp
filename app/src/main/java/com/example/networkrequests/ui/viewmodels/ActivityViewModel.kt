package com.example.networkrequests.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.networkrequests.ui.utils.NetworkUtils
import kotlinx.coroutines.launch

class ActivityViewModel( connectivityObserver: NetworkUtils.ConnectivityObserver) : ViewModel() {
 var connectivityObserver : NetworkUtils.ConnectivityObserver
 val mutableConnectionStatus = MutableLiveData<NetworkUtils.ConnectivityStatus>()

 init {
     this.connectivityObserver = connectivityObserver
    viewModelScope.launch {
        connectivityObserver.observe().collect{
         mutableConnectionStatus.value = it
        }
    }
 }
}
class ActivityViewModelFactory(val connectivityObserver: NetworkUtils.ConnectivityObserver): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if(modelClass.isAssignableFrom(ActivityViewModel::class.java)) return ActivityViewModel(connectivityObserver) as T
         else throw UnsupportedOperationException()
    }
}