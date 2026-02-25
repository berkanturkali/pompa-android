package com.pompa.android.model.brands

import com.squareup.moshi.Json

data class Provider(
    val id: Int,
    val name: String,
    @param:Json(name = "logo_url")
    val logo: String,
    @param:Json(name = "is_manual")
    val isManual: Boolean = false
)