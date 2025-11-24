package com.pompa.android.model.provinces

import kotlinx.serialization.Serializable

@Serializable
data class Province(
    val id: Int,
    val name: String,
    val code: String
)