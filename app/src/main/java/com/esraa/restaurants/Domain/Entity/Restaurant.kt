package com.esraa.restaurants.Domain.Entity

import android.location.Address

data class Restaurant(val id:String,
                      val latitude:Double,
                      val longitude:Double,
                      val city:String?,
                      val address: String,
                      val name:String)