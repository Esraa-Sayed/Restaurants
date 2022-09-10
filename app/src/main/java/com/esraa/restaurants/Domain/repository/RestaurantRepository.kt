package com.esraa.restaurants.Domain.repository

import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.RequestDto
import io.reactivex.Single

interface RestaurantRepository {
    fun getRestaurant(requestDto: RequestDto):Single<DataState<List<Restaurant>>>
}