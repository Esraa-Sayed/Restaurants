package com.esraa.restaurants.Domain.dto

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

data class RequestDto (val latLong: LatLng , val LatLngBounds:LatLngBounds)