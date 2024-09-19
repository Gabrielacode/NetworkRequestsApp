package com.example.networkrequests.data.sources.database.entities.pageddbmodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SimplifiedDummyItem(
    @PrimaryKey
    val id :Int, // No Auto Generated
    val title:String,
    val description:String,
    val thumbnailUrl:String //We wil still load the image from the Internet using Glide we are not storing it on the phone

)
