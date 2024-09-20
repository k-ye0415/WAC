package com.ioad.wac.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        val tvMainClothes:TextView
        init {
            ivMainClothes = itemView.findViewById(R.id.iv_main_clothes)
            tvMainClothes = itemView.findViewById(R.id.tv_main_clothes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.main_clothes_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clothes = clothesList.get(position)
        clothes.imageUri.let {
            glide.load(clothesList.get(position).imageUri)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.sunglasses)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).centerInside().into(holder.ivMainClothes)
        }
        holder.tvMainClothes.text = clothes.imageName
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }
}