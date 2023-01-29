package com.example.dziennikpodroznika.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dziennikpodroznika.databinding.ActivityDetailBinding
import com.example.dziennikpodroznika.models.PlaceModel
import com.example.dziennikpodroznika.utils.Constants

class DetailActivity : AppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    private var mPlacesModel: PlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if(intent.hasExtra(Constants.EXTRA_PLACE_DETAILS)){
            mPlacesModel = intent.getParcelableExtra(Constants.EXTRA_PLACE_DETAILS) as PlaceModel?
        }

        if(mPlacesModel != null){
            setupToolbar()
            binding?.ivPlaceImage?.setImageURI(Uri.parse(mPlacesModel!!.image))
            binding?.tvDescription?.text = mPlacesModel!!.description
            binding?.tvLocation?.text = mPlacesModel!!.location
        }

        binding?.btnViewOnMap?.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PLACE_DETAILS, mPlacesModel)
            startActivity(intent)
        }


    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbarPlaceDetail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = mPlacesModel!!.title
        binding?.toolbarPlaceDetail?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}