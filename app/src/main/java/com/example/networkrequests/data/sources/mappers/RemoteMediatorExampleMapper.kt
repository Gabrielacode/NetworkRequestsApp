package com.example.networkrequests.data.sources.mappers

import com.example.networkrequests.data.sources.database.entities.databasemodels.ProductApiResult
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.domain.model.DummyModel

fun Product.toSimpleDummyItem():SimplifiedDummyItem{
    return SimplifiedDummyItem(
        id = this.id,
        title = this.title,
        description = this.description,
        thumbnailUrl = this.thumbnail
    )
}
fun SimplifiedDummyItem.toDummyModel():DummyModel{
    return DummyModel(this.id,this.title,this.description,this.thumbnailUrl)
}
