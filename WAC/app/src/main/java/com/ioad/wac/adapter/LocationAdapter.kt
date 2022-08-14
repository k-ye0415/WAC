package com.ioad.wac.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ioad.wac.database.LocationDB
import com.ioad.wac.R
import com.ioad.wac.activity.MainBoardActivity2
import com.ioad.wac.database.Location
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LocationAdapter(
    val locationList: ArrayList<String>,
    val inflater: LayoutInflater,
    val context: Context,
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLocationItem: TextView

        init {

            tvLocationItem = itemView.findViewById(R.id.tv_location_item)
            itemView.setOnClickListener {
                val selectLocation = locationList.get(adapterPosition)

                val database = Room.databaseBuilder(
                    context,
                    LocationDB::class.java,
                    "location_database"
                ).allowMainThreadQueries().build()


                var locationData = Location(selectLocation, "Y", setDate(), "N", "null")

                database.locationDAO().insertLocation(locationData)


                val intent = Intent(context, MainBoardActivity2::class.java)
                intent.putExtra("SEARCH_LOCATION", selectLocation)
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


    fun setDate(): String {
        val cal = Calendar.getInstance()
        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)

        return date
    }
}