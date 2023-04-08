package com.shaderock.lunch.blpmini.infrastructure

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Failure<out T>(
        val error: Throwable,
        val data: T? = null,
        val onRetry: () -> Unit = {}
    ) : Resource<T>()

    fun success(onSuccess: (T) -> Unit) {
        if (this is Success) onSuccess(data)
    }

    fun loading(onLoading: (T?) -> Unit) {
        if (this is Loading) onLoading(data)
    }

    fun failure(onFailure: (Throwable) -> Unit) {
        if (this is Failure) onFailure(error)
    }
}
