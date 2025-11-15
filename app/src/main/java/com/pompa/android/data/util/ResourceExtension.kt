package com.pompa.android.data.util

import androidx.compose.runtime.MutableState
import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.util.Resource
import com.pompa.android.model.util.UIText
import kotlinx.coroutines.flow.Flow

suspend fun<T> Flow<Resource<BaseApiResponse<T>>>.collectResource(
    onError: (uiText: UIText?) -> Unit = {},
    loadingState: MutableState<Boolean>,
    onSuccess: (data: T?) -> Unit = {},
) {
     collect { responseResource ->
         when(responseResource) {
             is Resource.Error -> {
                loadingState.value = false
                 onError(responseResource.uiText)
             }
             is Resource.Loading -> {
                loadingState.value = true
             }
             is Resource.Success -> {
                loadingState.value = false
                 onSuccess(responseResource.data?.data)
             }
         }
     }
}