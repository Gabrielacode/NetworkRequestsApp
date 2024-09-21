package com.example.networkrequests.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networkrequests.data.sources.database.dao.PagedItemDao
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PagedItemRepository(val dao: PagedItemDao) {
    fun getListOfItems(): Flow<PagingData<PagedItem>>{
        return Pager(
            PagingConfig(20, prefetchDistance = 40)
        ){dao.getAllItems()}.flow
    }
    //Prefetch distance is the amount of distance of loaded content will be loaded from the recently loaded item
   suspend fun insertItem(item: PagedItem) =  withContext(Dispatchers.IO){ dao.insertItem(item)}
    suspend fun  deleteItem(item: PagedItem) = withContext(Dispatchers.IO){ dao.deleteItem(item)}
}