package com.esraa.restaurants.Core.di

import android.content.Context
import com.esraa.restaurants.BuildConfig
import com.esraa.restaurants.Core.Common.AppConst.BASE_URL
import com.esraa.restaurants.Core.Common.AppConst.TIME_OUT_VALUE
import com.esraa.restaurants.Data.api.FoursuareAPI
import com.esraa.restaurants.Data.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun providesLoggingInterceptor():HttpLoggingInterceptor{
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG){
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        return loggingInterceptor
    }
    @Singleton
    @Provides
    fun providesAthuInterceptor(@ApplicationContext applicationContext: Context):AuthInterceptor{
       return AuthInterceptor(applicationContext)
    }
    @Singleton
    @Provides
    fun providesOkhttpClient(authInterceptor: AuthInterceptor,loggingInterceptor: HttpLoggingInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIME_OUT_VALUE,TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_VALUE,TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_VALUE,TimeUnit.SECONDS)
            .build()
    }
    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient):Retrofit.Builder{
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }
    @Singleton
    @Provides
    fun providesFoursquareAPI(retrofit: Retrofit.Builder):FoursuareAPI{
        return retrofit.baseUrl(BASE_URL)
            .build().create(FoursuareAPI::class.java)
    }

}