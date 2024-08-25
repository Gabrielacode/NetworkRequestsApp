package com.example.networkrequests.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductApiResult(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)