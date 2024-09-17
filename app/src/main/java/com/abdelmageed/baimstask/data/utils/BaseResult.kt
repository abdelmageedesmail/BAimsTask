package com.abdelmageed.baimstask.data.utils


sealed class BaseResult<out T : Any, out U : Any> {
    @androidx.annotation.Keep
    data class Success<T : Any>(val data: T) : BaseResult<T, Nothing>()

    @androidx.annotation.Keep
    data class Error<U : Any>(val error: U) : BaseResult<Nothing, U>()
}