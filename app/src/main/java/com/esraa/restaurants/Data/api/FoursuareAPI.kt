package com.esraa.restaurants.Data.api
import com.esraa.restaurants.Data.api_response.NewResponse.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FoursuareAPI {
 //   @Headers("Authorization: fsq3asfuFbQhDQ50aLxEfN91aIP2xPCEHtFpwnaM4VTi3OY=")
   // @Headers({"Accept: application/json"})
    @GET("v3/places/search")
    fun getRestaurants(@Query("ll",encoded = true)ll:String):Single<Response>
}