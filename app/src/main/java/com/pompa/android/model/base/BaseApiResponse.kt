package com.pompa.android.model.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)