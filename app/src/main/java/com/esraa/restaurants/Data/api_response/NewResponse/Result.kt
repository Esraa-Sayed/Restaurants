package com.esraa.restaurants.Data.api_response.NewResponse

data class Result(
    val categories: List<Category>,
    val chains: List<Chain>,
    val distance: Int,
    val fsq_id: String,
    val geocodes: Geocodes,
    val link: String,
    val location: Location,
    val name: String,
    val related_places: RelatedPlaces,
    val timezone: String
)