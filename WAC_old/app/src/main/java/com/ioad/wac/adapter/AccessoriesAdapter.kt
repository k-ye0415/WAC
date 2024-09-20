package com.ioad.wac.adapter

import android.graphics.drawable.Drawable
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
import com.ioad.wac.R
import com.ioad.wac.model.Accessories

class AccessoriesAdapter(
    val accessoriesList:ArrayList<Accessories>,
    val inflater: LayoutInflater,
    val glide: RequestManager
) :RecyclerView.Adapter<AccessoriesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivMainAcc:ImageView
        val tvMainAccName:TextView
        init {
            ivMainAcc = itemView.findViewById(R.id.iv_main_accessories)
            tvMainAccName = itemView.findViewById(R.id.tv_main_accessories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.main_accessories_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accessories = accessoriesList.get(position)
        accessories.accessoriesUri.let {
            glide.load(accessories.accessoriesUri)
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
                }).centerInside().into(holder.ivMainAcc)
        }
        holder.tvMainAccName.text = accessories.accessoriesName
    }

    override fun getItemCount(): Int {
        return accessoriesList.size
    }
}