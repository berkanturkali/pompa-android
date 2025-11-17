package com.pompa.android.model.brands

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    val id: Int,
    val name: String,
    @SerialName("logo_url")
    val logo: String
)