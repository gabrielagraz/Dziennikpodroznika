package com.example.dziennikpodroznika.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toolbar
import com.example.dziennikpodroznika.R
import com.example.dziennikpodroznika.databinding.ActivityMapsBinding
import com.example.dziennikpodroznika.models.PlaceModel
import com.example.dziennikpodroznika.utils.Constants
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding: ActivityMapsBinding? = null

    private var mPlaceModel: PlaceModel? = null

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mapView = binding?.map!!

        if(intent.hasExtra(Constants.EXTRA_PLACE_DETAILS)){
            mPlaceModel = intent.getParcelableExtra(Constants.EXTRA_PLACE_DETAILS) as PlaceModel?
        }

        if(mPlaceModel != null){
            setupToolbar()
            mapView.getMapAsync(this)
            mapView.onCreate(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbarMaps)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = mPlaceModel!!.title
        binding?.toolbarMaps?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {

        if(mPlaceModel?.latitude != null && mPlaceModel?.longitude != null) {
            val position = LatLng(mPlaceModel!!.latitude, mPlaceModel!!.longitude)
            map.addMarker(MarkerOptions().position(position).title((mPlaceModel!!.location)))
            val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
            map.animateCamera(newLatLngZoom)
        }
    }
}