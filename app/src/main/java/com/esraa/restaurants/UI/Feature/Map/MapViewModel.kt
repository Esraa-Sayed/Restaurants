package com.esraa.restaurants.UI.Feature.Map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esraa.restaurants.Core.Common.BaseViewModel
import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.LocationDto
import com.esraa.restaurants.Domain.interactor.GetRestaurants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
@HiltViewModel
class MapViewModel @Inject constructor(private val getRestaurants: GetRestaurants):BaseViewModel() {
    private val _restaurantState = MutableLiveData<DataState<List<Restaurant>>>()

    val restaurantState:LiveData<DataState<List<Restaurant>>>
        get() = _restaurantState

    fun getRestaurantsFun(locationDto: LocationDto){
        if (_restaurantState.value != null) return
        getRestaurants.execute(locationDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                restaurants -> _restaurantState.value = restaurants
            }
            .also { compositeDispoable.add(it) }
    }

}