package com.example.networkrequests.data.sources.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem

@Dao
interface SimpleDummyDAO {
    //Here we will have three methods
    //1 TO Insert all data loaded
    //To get the data as a paging source and
    //To Delete all data in case of a refresh

    @Upsert
    suspend fun insertAll(simpledummyitems:List<SimplifiedDummyItem>)
    @Query("SELECT * FROM SimplifiedDummyItem")
    fun getAllItems(): PagingSource<Int, SimplifiedDummyItem>

    @Query("DELETE  FROM SimplifiedDummyItem")
    suspend fun deleteAll()
}