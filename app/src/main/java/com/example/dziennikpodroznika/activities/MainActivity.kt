package com.example.dziennikpodroznika.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikpodroznika.databinding.ActivityMainBinding
import com.example.dziennikpodroznika.adapters.Adapter
import com.example.dziennikpodroznika.database.DatabaseHandler
import com.example.dziennikpodroznika.models.PlaceModel
import com.example.dziennikpodroznika.utils.Constants
import com.example.dziennikpodroznika.utils.SwipeToDeleteCallback
import com.example.dziennikpodroznika.utils.SwipeToEditCallback


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getPlacesFromLocalDB()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddPlace?.setOnClickListener {
            val intent = Intent(this, AddPlace::class.java)
            resultLauncher.launch(intent)
        }

        getPlacesFromLocalDB()

    }

    private fun getPlacesFromLocalDB(){
        val handler = DatabaseHandler(this)
        val placesList: ArrayList<PlaceModel> = handler.getPlacesList()

        if(placesList.size > 0){
            binding?.rvPlacesList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
            setPlacesRecyclerView(placesList)
        } else {
            binding?.rvPlacesList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }

    }

    private fun setPlacesRecyclerView(list: ArrayList<PlaceModel>){
        binding?.rvPlacesList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvPlacesList?.setHasFixedSize(true)
        val adapter = Adapter(this, list)
        binding?.rvPlacesList?.adapter = adapter

        adapter.setOnClickListener(object : Adapter.OnClickListener{
            override fun onClick(position: Int, model: PlaceModel) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(Constants.EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        enableSwipeToDelete(adapter)
        enableSwipeToEdit(adapter)
    }

    private fun enableSwipeToDelete(adapter: Adapter) {
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding?.rvPlacesList)
    }

    private fun enableSwipeToEdit(adapter: Adapter) {
        val swipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding?.rvPlacesList)
    }

}