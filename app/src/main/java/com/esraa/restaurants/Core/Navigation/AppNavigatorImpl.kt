package com.esraa.restaurants.Core.Navigation

import androidx.fragment.app.FragmentActivity
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.R
import com.esraa.restaurants.UI.Feature.Map.RestaurantMapFragment
import com.esraa.restaurants.UI.Feature.Restaurant.RestaurantDetails
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(val activity: FragmentActivity):AppNavigator {
    override fun navigateTo(screen: Screen,restaurant: Restaurant?) {
        val fragment = when(screen){
            Screen.MAP -> RestaurantMapFragment()
            Screen.RESTAURANT -> RestaurantDetails.newInstance(restaurant)
        }
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.homeContianer,fragment)
            .addToBackStack(fragment.javaClass.canonicalName)
            .commit()
    }
}