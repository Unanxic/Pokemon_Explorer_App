package com.example.domain.models.network

sealed interface NetworkResult<T>

class ApiSuccess<T>(val data: T?) : NetworkResult<T>
class ApiError<T>(val apiError: ApiErrorModel? = null) : NetworkResult<T>
class ApiNoInternetError<T> : NetworkResult<T>
class ApiException<T>(val e: Exception) : NetworkResult<T>
class ApiLoading<T> : NetworkResult<T>

fun <T> NetworkResult<T>.getSuccessOrDefault(default: T? = null) = (this as? ApiSuccess)?.data ?: default
fun <T> NetworkResult<T>.getNetworkErrorOrNull() = (this as? ApiError)
fun <T> NetworkResult<T>.getNoInternetErrorOrNull() = (this as? ApiNoInternetError)
fun <T> NetworkResult<T>.getExceptionOrNull() = (this as? ApiException)
fun <T> NetworkResult<T>.getLoadingOrNull() = (this as? ApiLoading)
fun <T> NetworkResult<T>.isError() = (this is ApiError || this is ApiException || this is ApiNoInternetError)
fun <T> NetworkResult<T>.isLoading() = (this is ApiLoading)
fun <T> NetworkResult<T>.isSuccess() = (this is ApiSuccess)
fun <T, S> NetworkResult<T>.mapToType(mapping: (T?) -> S): NetworkResult<S> {
    return when (this) {
        is ApiError -> ApiError(apiError)
        is ApiException -> ApiException(e)
        is ApiLoading -> ApiLoading()
        is ApiNoInternetError -> ApiNoInternetError()
        is ApiSuccess -> ApiSuccess(data = mapping(data))
    }
}
fun <T> NetworkResult<T>.modifyApiErrorBy(apiErrorMapping: (ApiErrorModel?) -> NetworkResult<T>): NetworkResult<T> {
    return when (this) {
        is ApiError -> apiErrorMapping(this.apiError)
        is ApiException -> ApiException(e)
        is ApiLoading -> ApiLoading()
        is ApiNoInternetError -> ApiNoInternetError()
        is ApiSuccess -> ApiSuccess(data = this.data)
    }
}