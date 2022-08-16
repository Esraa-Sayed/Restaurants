package com.esraa.restaurants.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.esraa.restaurants.R
import com.esraa.restaurants.UI.Feature.Map.RestaurantMapFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeContianer,RestaurantMapFragment())
                .commit()
    }
}