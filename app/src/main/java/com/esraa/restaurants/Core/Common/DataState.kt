package com.esraa.restaurants.Core.Common

import com.esraa.restaurants.Domain.Error.Failure

sealed class DataState<out T>{
    data class Success<out T>(val data:T): DataState<T>()
    data class Error(val error: Failure): DataState<Nothing>()
    object  Loading:DataState<Nothing>()

}