package com.example.networkrequests.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val comment: String,
    val date: String,
    val rating: Int,
    val reviewerEmail: String,
    val reviewerName: String
)