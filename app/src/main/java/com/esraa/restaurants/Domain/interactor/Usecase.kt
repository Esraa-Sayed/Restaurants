package com.esraa.restaurants.Domain.interactor

interface Usecase<T,R> {
    fun execute(param:T):R
}