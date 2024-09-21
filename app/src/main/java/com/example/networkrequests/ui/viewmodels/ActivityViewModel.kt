package com.example.networkrequests.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.networkrequests.ui.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel //This shows a hilt view model which hilt will create for us we have to put @Inject in the constructor
//We can then call it in an activity or fragment that is annotated with @AndroidEntryPoint
class ActivityViewModel @Inject constructor( val connectivityObserver: NetworkUtils.ConnectivityObserver) : ViewModel() {

 val mutableConnectionStatus = MutableLiveData<NetworkUtils.ConnectivityStatus>()

 init {

    viewModelScope.launch {
        connectivityObserver.observe().collect{
         mutableConnectionStatus.value = it
        }
    }
 }
}
//class ActivityViewModelFactory(val connectivityObserver: NetworkUtils.ConnectivityObserver): ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        return if(modelClass.isAssignableFrom(ActivityViewModel::class.java)) return ActivityViewModel(connectivityObserver) as T
//         else throw UnsupportedOperationException()
//    }
//}
//We dont need this now