package com.example.networkrequests.data.sources.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.networkrequests.data.sources.database.dao.PagedItemDao
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database([PagedItem::class], version = 1)
abstract class PagedItemDatabase :RoomDatabase(){
    abstract fun itemDAO(): PagedItemDao

    companion object{
        private var instance: PagedItemDatabase? = null

        fun returnInstance (context: Context): PagedItemDatabase {
            return instance ?: synchronized(this){
                val newInstance = Room.databaseBuilder(context, PagedItemDatabase::class.java,"paged_item")
                    .addCallback(object:Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            instance?.let { db->
                                val scope = CoroutineScope(Dispatchers.IO)
                                scope.launch {
                                    repeat(100){db.itemDAO().insertItem(PagedItem(0,"Haouse","Yuudjnd"))}
                                }

                            }

                        }
                    })
                        .build()

                instance = newInstance
                instance!!
            }

        }
    }
}