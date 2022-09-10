package com.esraa.restaurants.Data.repository

import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Data.api.FoursuareAPI
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.LocationDto
import com.esraa.restaurants.Domain.repository.RestaurantRepository
import io.reactivex.Single

class RestaurantRpositoryImpl (private val api:FoursuareAPI):RestaurantRepository {
    override fun getRestaurant(locationDto: LocationDto): Single<DataState<List<Restaurant>>> {
        var lon = 0.0
        var lat = 0.0
        return api.getRestaurants("${locationDto.lat},${locationDto.lng}")
            .map {
                val restList = ArrayList<Restaurant>()
                val x = it.results
                it.results.forEach {
                        rest->
                     if (rest.geocodes.roof == null) {
                         lon = rest.geocodes.main.longitude
                         lat = rest.geocodes.main.latitude
                     }
                    else{
                         lat= rest.geocodes.roof.latitude
                         lon = rest.geocodes.roof.longitude
                    }
                    val newRestaurant = Restaurant(
                        id = rest.fsq_id,
                        name = rest.name,
                        city = rest.location.cross_street,
                        address = rest.location.country,

                        latitude = lat,
                        longitude = lon
                    )
                    restList.add(newRestaurant)
                }
                DataState.Success(restList)
            }
    }
}