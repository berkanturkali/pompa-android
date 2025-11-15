package com.pompa.android.network.util

import com.pompa.android.R
import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.util.Resource
import com.pompa.android.model.util.UIText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ApiUtils {

    fun <T> fetchData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<BaseApiResponse<T>>
    ): Flow<Resource<BaseApiResponse<T>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = withContext(dispatcher) { apiCall() }
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        if (it.success) {
                            emit(Resource.Success(it))
                        } else {
                            emit(Resource.Error(uiText = UIText.DynamicString(it.message)))
                        }
                    } ?: emit(Resource.Error(UIText.StringResource(R.string.no_content)))
                }
            } catch (exception: Exception) {
                handleException(exception = exception)
            }
        }
    }

    private suspend fun <T> FlowCollector<Resource<T>>.handleException(exception: Exception) {
        when (exception) {
            is ConnectException -> {
                emit(Resource.Error(uiText = UIText.DynamicString(exception.message)))
            }

            is SocketTimeoutException -> {
                emit(Resource.Error(uiText = UIText.StringResource(R.string.request_timeout)))
            }

            is TimeoutCancellationException -> {
                emit(Resource.Error(uiText = UIText.StringResource(R.string.request_timeout)))
            }

            is IOException -> {
                emit(
                    Resource.Error(
                        uiText = exception.localizedMessage?.let { message ->
                            UIText.DynamicString(message)
                        }
                            ?: UIText.StringResource(R.string.network_connection_error)
                    ))
            }

            is HttpException -> {
                val error = exception.response()?.errorBody()?.parseError<Unit>()
                val message = error?.message
                emit(Resource.Error(uiText = UIText.DynamicString(message)))
            }

            is IllegalArgumentException -> {
                return
            }

            else -> {
                emit(Resource.Error(uiText = UIText.StringResource(R.string.something_went_wrong)))
            }
        }
    }

    inline fun <reified T> ResponseBody.parseError(): BaseApiResponse<T>? {
        return try {
            Json.decodeFromString<BaseApiResponse<T>>(string())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}