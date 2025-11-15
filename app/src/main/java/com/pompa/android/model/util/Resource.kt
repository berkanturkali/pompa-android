package com.pompa.android.model.util

sealed class Resource<T>(
    val data: T? = null,
    val uiText: UIText? = null,
) {
    class Success<T>(data: T? = null) : Resource<T>(data)

    class Error<T>(uiText: UIText, data: T? = null) : Resource<T>(data, uiText)

    class Loading<T>(data: T? = null) : Resource<T>(data)
}