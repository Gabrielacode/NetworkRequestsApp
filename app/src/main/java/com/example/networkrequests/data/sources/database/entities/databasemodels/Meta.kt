package com.example.networkrequests.data.sources.database.entities.databasemodels

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val barcode: String,
    val createdAt: String,
    val qrCode: String,
    val updatedAt: String
)