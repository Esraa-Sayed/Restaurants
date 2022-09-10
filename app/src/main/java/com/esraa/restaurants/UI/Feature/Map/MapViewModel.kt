package com.esraa.restaurants.UI.Feature.Map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esraa.restaurants.Core.Common.BaseViewModel
import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.dto.RequestDto
import com.esraa.restaurants.Domain.interactor.GetRestaurants
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
@HiltViewModel
class MapViewModel @Inject constructor(private val getRestaurants: GetRestaurants):BaseViewModel() {
    private val _restaurantState = MutableLiveData<DataState<List<Restaurant>>>()
    var fragmentRecreated = false
    val restaurantState:LiveData<DataState<List<Restaurant>>>
        get() = _restaurantState

    val markers = HashMap<Marker,Restaurant>()
    fun getRestaurantsFun(requestDto:RequestDto){
        if (_restaurantState.value != null) return
        _restaurantState.value = DataState.Loading
        getRestaurants.execute(requestDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                restaurants -> _restaurantState.value = restaurants
            }
            .also { compositeDispoable.add(it) }
    }

    fun restRestaurantState() {
        _restaurantState.value = null
    }
    fun getNewRestaurants(restaurants:List<Restaurant> ) : ArrayList<Restaurant> {
        val markersToBeDisplayed = ArrayList<Restaurant>()
        val mainList = markers.values

        if (mainList.isNullOrEmpty()){
            restaurants.forEach{
                if(!mainList.contains(it))
                    markersToBeDisplayed.add(it)
            }
        }else{
            markersToBeDisplayed.addAll(restaurants)
        }
        return markersToBeDisplayed
    }
}