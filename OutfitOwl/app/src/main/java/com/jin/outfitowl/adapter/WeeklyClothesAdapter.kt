package com.jin.outfitowl.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jin.outfitowl.databinding.ItemClothesBinding
import com.jin.outfitowl.util.TAG

class WeeklyClothesAdapter(private val clothesList: List<String>, val context: Context) :
    RecyclerView.Adapter<WeeklyClothesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeeklyClothesAdapter.ViewHolder {
        val binding = ItemClothesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeeklyClothesAdapter.ViewHolder, position: Int) {
        val clothes = clothesList[position]
        Log.e(TAG.TEST.label,"흠.... 여기를 탈텐디 $clothes")
        Glide.with(context).load(clothes).into(holder.binding.ivClothes)
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }

    class ViewHolder(val binding: ItemClothesBinding) : RecyclerView.ViewHolder(binding.root)
}