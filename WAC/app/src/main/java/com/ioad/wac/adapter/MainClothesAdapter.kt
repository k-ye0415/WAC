package com.ioad.wac.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.ioad.wac.R
import com.ioad.wac.model.Clothes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainClothesAdapter(
    val clothesList:ArrayList<Clothes>,
    val inflater: LayoutInflater,
    val glide:RequestManager
):RecyclerView.Adapter<MainClothesAdapter.ViewHolder>() {
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivMainClothes:ImageView
        init {
            ivMainClothes = itemView.findViewById(R.id.iv_main_clothes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.main_clothes_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clothes = clothesList.get(position)
        clothes.imageUri.let {
            glide.load(it).centerInside().into(holder.ivMainClothes)
        }
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }
}