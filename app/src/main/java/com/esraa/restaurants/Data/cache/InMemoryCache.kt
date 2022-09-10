package com.esraa.restaurants.Data.cache

import com.esraa.restaurants.Domain.Entity.Restaurant

object InMemoryCache {
    private val cache = ArrayList<Restaurant>()
    fun get() = cache
    fun add(restaurant: List<Restaurant>){
          cache.addAll(restaurant)
    }
}