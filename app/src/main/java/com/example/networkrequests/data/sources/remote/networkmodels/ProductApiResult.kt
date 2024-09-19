package com.example.networkrequests.data.sources.remote.networkmodels

import kotlinx.serialization.Serializable

@Serializable
data class ProductApiResult(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)