package com.example.networkrequests.data.sources.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem

@Dao
interface PagedItemDao {

    @Query("SELECT * FROM PagedItem")
     fun getAllItems():PagingSource<Int, PagedItem>
    //Room supports  returning a Paging Source for a list of items which promotes pagination in room

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PagedItem)
    @Delete
    suspend fun deleteItem(item: PagedItem)
}