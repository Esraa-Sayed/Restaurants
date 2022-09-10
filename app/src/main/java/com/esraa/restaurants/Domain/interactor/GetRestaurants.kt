package com.esraa.restaurants.Domain.interactor

import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.Error.ErrorHandler
import com.esraa.restaurants.Domain.Error.Failure
import com.esraa.restaurants.Domain.dto.LocationDto
import com.esraa.restaurants.Domain.repository.RestaurantRepository
import io.reactivex.Single
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.inject.Inject

class GetRestaurants @Inject constructor(private val repository: RestaurantRepository):Usecase<LocationDto, Single<DataState<List<Restaurant>>>>,ErrorHandler{
    override fun execute(param: LocationDto): Single<DataState<List<Restaurant>>> {
        return repository.getRestaurant(param).onErrorReturn {
                DataState.Error(getError(it))
        }
    }

    override fun getError(throwable: Throwable): Failure {
        return when(throwable){
            is UnknownHostException -> Failure.NetworkConnection
            is HttpException ->{
                when(throwable.code()){
                     HttpURLConnection.HTTP_NOT_FOUND ->Failure.ServerError.NotFound
                    //access denied
                     HttpURLConnection.HTTP_FORBIDDEN ->Failure.ServerError.AccessDenied
                     HttpURLConnection.HTTP_UNAVAILABLE ->Failure.ServerError.ServiceUnAvailable
                    else-> Failure.Unknown

                }
            }
            else-> Failure.Unknown
        }
    }
}