package com.example.networkrequests.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import com.example.networkrequests.data.pagingexample.PagedExampleRemoteMediator
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.data.sources.remote.services.SortOrder

class PagedProductsFromNetworkAndCache @OptIn(ExperimentalPagingApi::class) constructor(
    val db:SimpleDummyDatabase,
    val apiService:DummyJsonApi,
    ) {
    @OptIn(ExperimentalPagingApi::class)
    fun getList()=Pager<Int,SimplifiedDummyItem>(PagingConfig(10), remoteMediator = PagedExampleRemoteMediator(db,apiService,"id",
        SortOrder.ASC)){
       db.simpleDao().getAllItems()
    }.flow
    @OptIn(ExperimentalPagingApi::class)
    fun getListSortedby(value: String, sortOrder: SortOrder?) =
         Pager<Int,SimplifiedDummyItem>(PagingConfig(10), remoteMediator = PagedExampleRemoteMediator(db,apiService,value, order = sortOrder?:SortOrder.ASC))
        {db.simpleDao().getAllItems()}.flow

    //In here we just specify our remote mediator
}