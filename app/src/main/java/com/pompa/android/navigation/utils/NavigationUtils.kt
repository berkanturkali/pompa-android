package com.pompa.android.navigation.utils

import android.util.Base64
import kotlinx.serialization.json.Json


val navJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}

inline fun <reified T> encodeNavArg(value: T, json: Json = navJson): String {
    val jsonStr = json.encodeToString(value)
    return Base64.encodeToString(
        jsonStr.toByteArray(Charsets.UTF_8),
        Base64.URL_SAFE or Base64.NO_WRAP
    )
}

inline fun <reified T> decodeNavArg(payload: String, json: Json = navJson): T {
    val jsonStr = String(
        Base64.decode(payload, Base64.URL_SAFE),
        Charsets.UTF_8
    )
    return json.decodeFromString(jsonStr)
}