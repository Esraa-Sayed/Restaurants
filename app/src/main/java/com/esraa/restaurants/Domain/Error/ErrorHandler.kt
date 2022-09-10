package com.esraa.restaurants.Domain.Error

interface ErrorHandler {
    fun getError(throwable: Throwable):Failure
}