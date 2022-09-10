package com.esraa.restaurants.Domain.Error

sealed class Failure {
    object NetworkConnection:Failure()
    sealed class ServerError: Failure(){
        object NotFound:ServerError()
        object ServiceUnAvailable:ServerError()
        object AccessDenied:ServerError()
    }
    object Unknown:Failure()
}