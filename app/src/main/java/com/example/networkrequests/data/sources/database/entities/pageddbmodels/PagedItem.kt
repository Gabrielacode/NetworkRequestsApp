package com.example.networkrequests.data.sources.database.entities.pageddbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PagedItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String
)
