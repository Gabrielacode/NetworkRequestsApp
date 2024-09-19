package com.example.networkrequests.data.sources.database.entities.databasemodels

import kotlinx.serialization.Serializable

@Serializable
data class Dimensions(
    val depth: Double,
    val height: Double,
    val width: Double
)