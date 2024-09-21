package com.example.networkrequests.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.networkrequests.data.repository.PagedProductsFromNetworkAndCache
import com.example.networkrequests.data.sources.mappers.toDummyModel
import com.example.networkrequests.data.sources.remote.services.SortOrder
import com.example.networkrequests.domain.model.DummyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

@HiltViewModel
class PagedNetworkAndCacheViewModel @Inject constructor(val repo:PagedProductsFromNetworkAndCache) :ViewModel() {
   val dummyItemsPagedFlow = repo.getList()
       .map { it.map {
           it.toDummyModel()
       } .map { UserModel.UIModel(it) }

           .insertSeparators()
           { before: UserModel.UIModel?, after: UserModel.UIModel? ->
               when {
                   before == null -> {UserModel.SeperatorModel(after?.data?.title?.first().toString()?:"Headey")}
                   after == null -> UserModel.SeperatorModel("Footey")
                   (before.data.title.first() !=  after.data.title.first()) ->{UserModel.SeperatorModel(after.data.title.first().toString())}
                   else -> {
                       null
                   }
               }
           }
       }
       .cachedIn(viewModelScope)


    fun getListOfItems( value: String,sortOrder: SortOrder?)=
        repo.getListSortedby(value,sortOrder)
            .map{
                it.map {
                    it.toDummyModel()
                }.map { UserModel.UIModel(it) }

                    .insertSeparators()
                { before: UserModel.UIModel?, after: UserModel.UIModel? ->
                        when {
                            before == null -> {UserModel.SeperatorModel(after?.data?.title?.first().toString()?:"Headey")}
                            after == null -> UserModel.SeperatorModel("Footey")
                            (before.data.title.first() !=  after.data.title.first()) ->{UserModel.SeperatorModel(after.data.title.first().toString())}
                            else -> {
                                null
                            }
                        }
                }
            }.cachedIn(viewModelScope)
}
sealed class UserModel(){
    class UIModel( val data:DummyModel):UserModel()
    class SeperatorModel(var description:String):UserModel()
}
class PagedNetworkAndCacheViewModelFactory(val repo:PagedProductsFromNetworkAndCache):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return  if(modelClass.isAssignableFrom(PagedNetworkAndCacheViewModel::class.java)) PagedNetworkAndCacheViewModel(repo) as T
         else super.create(modelClass,extras)
    }
}