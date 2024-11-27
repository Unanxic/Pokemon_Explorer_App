package com.example.domain.models.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiErrorModel(
    val error: Error? = null,
    val statusCode: Long? = null,
    val message: String? = null,
)