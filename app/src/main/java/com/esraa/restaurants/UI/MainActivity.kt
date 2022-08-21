package com.esraa.restaurants.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.esraa.restaurants.Core.Navigation.AppNavigator
import com.esraa.restaurants.Core.Navigation.Screen
import com.esraa.restaurants.R
import com.esraa.restaurants.UI.Feature.Map.RestaurantMapFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var appNavigator: AppNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null)
           appNavigator.navigateTo(Screen.MAP)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 0)
            finish()
    }
}