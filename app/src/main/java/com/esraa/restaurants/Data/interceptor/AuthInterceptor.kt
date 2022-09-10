package com.esraa.restaurants.Data.interceptor

import android.content.Context
import com.esraa.restaurants.R
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val appContext: Context):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
      //  val originalUrl = request.url()
       // val modifiedUrl = originalUrl.newBuilder()
                       // .addQueryParameter(CLIENT_ID,appContext.getString(R.string.foursquare_client_id))
                        //.addQueryParameter(CLIENT_SECRET,appContext.getString(R.string.foursquare_client_secret))
                        //.addQueryParameter(VERSION, CURRENT_VERSION)
                        //.addQueryParameter(QUERY, DEFAULT_QUERY)

                           // .build()
        val modifiedRequest = request.newBuilder()
        modifiedRequest.header("Content-Type","application/json")
        modifiedRequest.header("Authorization",appContext.getString(R.string.Restaurants_API_KEY))

        return  chain.proceed(modifiedRequest.build())
    }
}