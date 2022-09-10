package com.esraa.restaurants.Data.api_response.NewResponse

data class Location(
    val address: String,
    val address_extended: String,
    val census_block: String,
    val country: String,
    val cross_street: String,
    val dma: String,
    val formatted_address: String,
    val locality: String,
    val postcode: String,
    val region: String
)