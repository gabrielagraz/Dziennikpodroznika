package com.example.dziennikpodroznika.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikpodroznika.databinding.ItemPlaceBinding
import com.example.dziennikpodroznika.activities.AddPlace
import com.example.dziennikpodroznika.activities.MainActivity
import com.example.dziennikpodroznika.models.PlaceModel
import com.example.dziennikpodroznika.utils.Constants

class Adapter (
    private val context: Context,
    private var list: ArrayList<PlaceModel>
) : RecyclerView.Adapter<Adapter.MainViewHolder>(){

    private var onClickListener: OnClickListener? = null

    inner class MainViewHolder (private val itemBinding: ItemPlaceBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(position: Int) {
            val item = list[position]

            itemView.setOnClickListener {
                if(onClickListener != null) {
                    onClickListener!!.onClick(position, item)
                }
            }

            itemBinding.ivPlaceImage.setImageURI(Uri.parse(item.image))
            itemBinding.tvTitle.text = item.title
            itemBinding.tvDescription.text = item.description


        }
    }

    interface  OnClickListener {
        fun onClick(position: Int, model: PlaceModel)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        ) }

    override fun onBindViewHolder(holder: MainViewHolder, Position: Int) {
        holder.bindItem(Position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun notifyEditItem(activity: MainActivity, position: Int){
        val intent = Intent(context, AddPlace::class.java)
        intent.putExtra(Constants.EXTRA_PLACE_DETAILS, list[position])
        activity.resultLauncher.launch(intent)
        notifyItemChanged(position)
    }

}