package com.jin.outfitowl.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jin.outfitowl.databinding.ItemClothesBinding
import com.jin.outfitowl.util.TAG

class AverageClothesAdapter(private val clothesList: List<String>, val context: Context) :
    RecyclerView.Adapter<AverageClothesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AverageClothesAdapter.ViewHolder {
        val binding = ItemClothesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AverageClothesAdapter.ViewHolder, position: Int) {
        val clothes = clothesList[position]
        Glide.with(context).load(clothes).into(holder.binding.ivClothes)
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }

    class ViewHolder(val binding: ItemClothesBinding) : RecyclerView.ViewHolder(binding.root)
}