package com.example.networkrequests.data.sources.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.networkrequests.data.sources.database.dao.SimpleDummyDAO
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem

@Database([SimplifiedDummyItem::class], version = 1)
 abstract class SimpleDummyDatabase : RoomDatabase() {
      abstract fun simpleDao():SimpleDummyDAO

      companion object{
          var INSTANCE :SimpleDummyDatabase? = null
               fun getInstance(context: Context):SimpleDummyDatabase{
               return INSTANCE ?: synchronized(this){
                   INSTANCE = Room.databaseBuilder(context,SimpleDummyDatabase::class.java,"simple_items")
                       .build()
                   INSTANCE!!
               }
           }
      }
}