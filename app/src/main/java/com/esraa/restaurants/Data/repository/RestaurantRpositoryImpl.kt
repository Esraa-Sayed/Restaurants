package com.esraa.restaurants.Data.repository

import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Data.api.FoursuareAPI
import com.esraa.restaurants.Data.cache.InMemoryCache
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.RequestDto
import com.esraa.restaurants.Domain.repository.RestaurantRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

class RestaurantRpositoryImpl (private val api:FoursuareAPI):RestaurantRepository {
    override fun getRestaurant(requestDto: RequestDto): Single<DataState<List<Restaurant>>> {
        val cach = InMemoryCache.get()
        var lon = 0.0
        var lat = 0.0
        val filterdData = ArrayList<Restaurant>()

        cach.forEach{
            val latLng = LatLng(it.latitude,it.longitude)
            if(requestDto.LatLngBounds.contains(latLng))
                filterdData.add(it)
        }
       if (!filterdData.isNullOrEmpty()){
            return Single.just( DataState.Success(filterdData))
        }
        return api.getRestaurants("${requestDto.latLong.latitude},${requestDto.latLong.longitude}")
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
                        address = "${rest.location.country}, ${rest.location.formatted_address}",

                        latitude = lat,
                        longitude = lon
                    )
                    restList.add(newRestaurant)
                }
                // add data to in memory cache
                InMemoryCache.add(restList)
                DataState.Success(restList)
            }
    }
}