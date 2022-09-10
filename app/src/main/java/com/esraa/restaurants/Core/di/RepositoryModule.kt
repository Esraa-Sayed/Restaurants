package com.esraa.restaurants.Core.di

import com.esraa.restaurants.Data.api.FoursuareAPI
import com.esraa.restaurants.Data.repository.RestaurantRpositoryImpl
import com.esraa.restaurants.Domain.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun providesRestaurantRepository(api:FoursuareAPI):RestaurantRepository{
        return RestaurantRpositoryImpl(api)
    }
}