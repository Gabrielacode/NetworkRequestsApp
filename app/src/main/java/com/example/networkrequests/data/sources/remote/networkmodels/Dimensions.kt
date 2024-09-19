package com.example.networkrequests.data.sources.remote.networkmodels

import kotlinx.serialization.Serializable

@Serializable
data class Dimensions(
    val depth: Double,
    val height: Double,
    val width: Double
)