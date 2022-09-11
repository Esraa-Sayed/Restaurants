package com.esraa.restaurants.UI.Feature.Map

import android.Manifest
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.esraa.restaurants.Core.Common.BaseFragment
import com.esraa.restaurants.Core.Common.DataState
import com.esraa.restaurants.Core.Navigation.AppNavigator
import com.esraa.restaurants.Core.Navigation.Screen
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.Domain.Error.Failure
import com.esraa.restaurants.Domain.dto.RequestDto
import com.esraa.restaurants.R
import com.esraa.restaurants.UI.Feature.Map.drag.IDragCallback
import com.esraa.restaurants.databinding.FragmentRestaurantMapBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
@AndroidEntryPoint
class RestaurantMapFragment : BaseFragment(),GoogleMap.OnMarkerClickListener,IDragCallback {
@Inject lateinit var appNavigator: AppNavigator
private val mapViewModel:MapViewModel by viewModels()
    private var googleMap:GoogleMap? = null
    private var _binding:FragmentRestaurantMapBinding? = null

    private val callback = OnMapReadyCallback { googleMap ->
       this.googleMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        googleMap.setMinZoomPreference(12f)
        observerRestaurants()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantMapBinding.inflate(inflater,container,false)
        _binding?.draggableLayout?.setDrag(this)
        mapViewModel.fragmentRecreated = (savedInstanceState != null)
        return _binding!!.root
    }
    private fun observerRestaurants() {
        mapViewModel.restaurantState.observe(viewLifecycleOwner, {
            when(it){
                is DataState.Error -> {
                    handleLoading(false)
                    if(it.error is Failure.NetworkConnection)
                        displayError(getString(R.string.Network_connection_error))
                    else
                        displayError(getString(R.string.generalError))

                }
                is DataState.Loading -> {
                    handleLoading(true)
                }
                is DataState.Success -> {
                    handleLoading(false)
                    renderMarkers(it.data)
                }
            }
        })
    }

    private val localSettingScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getCurrentLocation()
    }
    private val applicationSettingsScreen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getCurrentLocation()
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
                //call foursquare api to get restaurants
                val currentLatLng = LatLng(it.latitude,it.longitude)
                val currentBound = googleMap?.projection?.visibleRegion?.latLngBounds
                if(currentBound != null && currentLatLng != null)
                    mapViewModel.getRestaurantsFun(RequestDto(currentLatLng,currentBound))
                 // mapViewModel.getRestaurantsFun(LocationDto(29.8579334,31.3107331))
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        val restaurant = mapViewModel.markers[marker]
        appNavigator.navigateTo(Screen.RESTAURANT,restaurant)
        return false
    }
    private fun renderMarkers(venus:List<Restaurant>){
        googleMap?.clear()
        val newVenus = mapViewModel.getNewRestaurants(venus)
        newVenus.forEach {
            venue ->
            val loc = LatLng(venue.latitude,venue.longitude)
            val markerOption = MarkerOptions().position(loc).title(venue.name)
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
            val marker =googleMap?.addMarker(markerOption)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(loc))
            //googleMap?.addMarker(MarkerOptions().position(loc).title(venue.name))
            if(marker != null){
                mapViewModel.markers[marker] = venue
            }
        }
        if (newVenus.isEmpty() && mapViewModel.fragmentRecreated){
            mapViewModel.markers.values.forEach{
                    venue ->
                val loc = LatLng(venue.latitude,venue.longitude)
                val markerOption = MarkerOptions().position(loc).title(venue.name)
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                googleMap?.addMarker(markerOption)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(loc))
            }
        }
    }
    private fun handleLoading(isLoading:Boolean){
        _binding!!.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun displayError(message:String){
        message?.let {
            Snackbar.make(_binding!!.parentLayout,message,Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onDrag() {
        Timber.e("Drag Here")
        if (mapViewModel.fragmentRecreated)
            mapViewModel.fragmentRecreated = false
        val currentLatLng = googleMap?.cameraPosition?.target
        Timber.e("Available lat , long : %s,%s",currentLatLng?.latitude,currentLatLng?.longitude)
        val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds
        mapViewModel.restRestaurantState()
        if(currentBounds != null && currentLatLng != null){
            mapViewModel.getRestaurantsFun(RequestDto(currentLatLng,currentBounds))
        }
    }

    //MARK end
}