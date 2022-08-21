package com.esraa.restaurants.Core.Navigation

interface AppNavigator {
    fun navigateTo(screen:Screen)
}
enum class Screen{
    MAP,
    RESTAURANT
}