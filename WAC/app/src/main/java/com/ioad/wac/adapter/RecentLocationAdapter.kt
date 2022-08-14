package com.ioad.wac.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ioad.wac.DiffUtilCallback
import com.ioad.wac.R
import com.ioad.wac.database.Location
import com.ioad.wac.database.LocationDB
import com.ioad.wac.model.Locations
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecentLocationAdapter(
    val recentLocationList:ArrayList<Locations>,
    val inflater: LayoutInflater,
    val context: Context
) : RecyclerView.Adapter<RecentLocationAdapter.ViewHolder>(){

    val database = Room.databaseBuilder(
        context,
        LocationDB::class.java,
        "location_database"
    ).allowMainThreadQueries().build()

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvRecentLocation:TextView
        val btnBookmark:TextView
        val btnDelete:TextView

        init {
            val database = Room.databaseBuilder(
                context,
                LocationDB::class.java,
                "location_database"
            ).allowMainThreadQueries().build()


            tvRecentLocation = itemView.findViewById(R.id.tv_recent_location_item)
            btnBookmark = itemView.findViewById(R.id.btn_bookmark)
            btnDelete = itemView.findViewById(R.id.btn_delete)


//            var locationData = Location(selectLocation, "Y", setDate(), "N", "null")
//
//            database.locationDAO().insertLocation(locationData)

//            btnBookmark.setOnClickListener {
//                Log.e("TAG", "bookmark Stauts :: " + recentLocationList.get(adapterPosition).bookmark)
//                val bookmarkStatus = recentLocationList.get(adapterPosition).bookmark
//                if (bookmarkStatus.equals("Y")) {
//                    database.locationDAO().updateRecentBookmark("N", recentLocationList.get(adapterPosition).id.toInt())
//                } else {
//                    database.locationDAO().updateRecentBookmark("Y", recentLocationList.get(adapterPosition).id.toInt())
//                }
//
//            }


            btnDelete.setOnClickListener {
                database.locationDAO().updateRecentLocationDelete("Y", setDate(), recentLocationList.get(adapterPosition).id.toInt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.recent_location_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.tvRecentLocation.text = recentLocationList.get(position).location
        if (recentLocationList.get(position).bookmark.equals("Y")) {
            holder.btnBookmark.setBackgroundResource(R.drawable.bookmark)
        }

        holder.btnBookmark.setOnClickListener {
            Log.e("TAG", "bookmark Stauts :: " + recentLocationList.get(position).bookmark)
            val bookmarkStatus = recentLocationList.get(position).bookmark
            if (bookmarkStatus.equals("Y")) {
                database.locationDAO().updateRecentBookmark("N", recentLocationList.get(position).id.toInt())
            } else {
                database.locationDAO().updateRecentBookmark("Y", recentLocationList.get(position).id.toInt())
            }
            changeList(reloadList())
        }


    }

    override fun getItemCount(): Int {
        return recentLocationList.size
    }

    fun reloadList():ArrayList<Locations> {
        val newList = ArrayList<Locations>()
        val recentLocationList = database.locationDAO().getRecentLocationList()
        recentLocationList.forEach {
            val locations = Locations(
                it.id.toString(),
                it.location,
                it.bookmark,
                it.saveDate,
                it.deleteStatus,
                it.deleteDate
            )
            newList.add(locations)
        }
        return newList
    }


    fun changeList(list:ArrayList<Locations>) {
        val diffUtilCallback = DiffUtilCallback(this.recentLocationList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.recentLocationList.apply {
            clear()
            addAll(list)
            diffResult.dispatchUpdatesTo(this@RecentLocationAdapter)
        }
    }



    fun setDate(): String {
        val cal = Calendar.getInstance()
        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)

        return date
    }
}