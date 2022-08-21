package com.esraa.restaurants.Core.di

import androidx.core.app.ActivityCompat
import com.esraa.restaurants.Core.Navigation.AppNavigator
import com.esraa.restaurants.Core.Navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule {
    @Binds
    abstract fun bindAppNavigator(appNavigator:AppNavigatorImpl):AppNavigator
}
