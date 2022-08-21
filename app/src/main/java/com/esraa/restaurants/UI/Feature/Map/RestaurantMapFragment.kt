package com.esraa.restaurants.UI.Feature.Map

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.esraa.restaurants.Core.Common.BaseFragment
import com.esraa.restaurants.Core.Navigation.AppNavigator
import com.esraa.restaurants.Core.Navigation.Screen
import com.esraa.restaurants.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
@AndroidEntryPoint
class RestaurantMapFragment : BaseFragment(),GoogleMap.OnMarkerClickListener {
@Inject lateinit var appNavigator: AppNavigator
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerClickListener(this)
    }
    private val localSettingScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getCurrentLocation()
    }
    private val applicationSettingsScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getCurrentLocation()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        getCurrentLocationWithPermissionCheck()
    }
    //MARK -- handle permissions
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation(){
        if (isLocationEnabled()){
            getLastKnownLocation {
                Timber.e("Available lat , long : %s,%s",it.latitude,it.longitude)
            }
        }else{
            MaterialAlertDialogBuilder(getRootActivity())
                .setTitle(getString(R.string.Location_not_enabled))
                .setMessage(getString(R.string.Please_enable_your_location))
                .setPositiveButton(getString(R.string.Enable)){
                    dialog,_ ->
                    openSettingsScreen()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.Deny)){
                        dialog,_ ->
                    dialog.dismiss()
                }
                .show()

        }
    }

    private fun openSettingsScreen() {
        localSettingScreen.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onShowRationaleLocation(request:PermissionRequest){
        MaterialAlertDialogBuilder(getRootActivity())
            .setTitle(getString(R.string.Location_not_enabled))
            .setMessage(getString(R.string.Please_enable_your_location))
            .setPositiveButton(getString(R.string.Enable)){
                    dialog,_ ->
                request.proceed()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.Deny)){
                    dialog,_ ->
                request.cancel()
                dialog.dismiss()
            }
            .show()
    }//Never Ask Again
    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onDenyAskLocation(){
            Toast.makeText(getRootActivity(),getString(R.string.Location_denied),Toast.LENGTH_LONG).show()
    }
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskLocation(){
        val openApplicationSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        openApplicationSettings.data = Uri.fromParts("package",activity?.packageName,null)
        applicationSettingsScreen.launch(openApplicationSettings)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        this.onRequestPermissionsResult(requestCode,grantResults)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        appNavigator.navigateTo(Screen.RESTAURANT)
        return false
    }
    //MARK end
}