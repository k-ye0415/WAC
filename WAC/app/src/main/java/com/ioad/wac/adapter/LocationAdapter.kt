package com.ioad.wac.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ioad.wac.R
import com.ioad.wac.activity.MainBoardActivity2

class LocationAdapter(
    val locationList:ArrayList<String>,
    val inflater: LayoutInflater,
    val context:Context
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>(){
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvLocationItem:TextView
        init {

            tvLocationItem = itemView.findViewById(R.id.tv_location_item)
            itemView.setOnClickListener {
                Toast.makeText(context, locationList.get(adapterPosition), Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainBoardActivity2::class.java)
                intent.putExtra("SEARCH_LOCATION", locationList.get(adapterPosition))
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.location_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvLocationItem.text = locationList.get(position)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}