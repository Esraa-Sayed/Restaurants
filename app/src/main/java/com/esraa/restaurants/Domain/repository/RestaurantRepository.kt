package com.esraa.restaurants.Domain.repository

import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.LocationDto
import io.reactivex.Single

interface RestaurantRepository {
    fun getRestaurant(locationDto:LocationDto):Single<DataState<List<Restaurant>>>
}