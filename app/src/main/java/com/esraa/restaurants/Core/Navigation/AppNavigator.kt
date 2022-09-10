package com.esraa.restaurants.Core.Navigation

import com.esraa.restaurants.Domain.Entity.Restaurant

interface AppNavigator {
    fun navigateTo(screen: Screen,restaurant: Restaurant? = null)
}
enum class Screen{
    MAP,
    RESTAURANT
}