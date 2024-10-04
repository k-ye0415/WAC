package com.jin.outfitowl.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.jin.outfitowl.R
import com.jin.outfitowl.data.AverageWeather
import com.jin.outfitowl.data.ClothesTemp
import com.jin.outfitowl.databinding.ItemWeeklyBinding
import com.jin.outfitowl.util.TAG

class WeeklyWeatherAdapter(private val weeklyList: List<AverageWeather>, val context: Context) :
    RecyclerView.Adapter<WeeklyWeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeeklyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weeklyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderInterface -> holder.bind()
        }

    }

    inner class ViewHolder(val binding: ItemWeeklyBinding) : RecyclerView.ViewHolder(binding.root),
        ViewHolderInterface {
        override fun bind() {
            val weekly = weeklyList[adapterPosition]
            binding.tvDay.text = weekly.day
            // 날씨 아이콘 설정
            val icon = if (weekly.icon.contains("01")) {
                if (weekly.icon == "01d") R.drawable.ic_clear_day else R.drawable.ic_clear_night
            } else {
                "https://openweathermap.org/img/wn/${weekly.icon}@2x.png"
            }
            Glide.with(context).load(icon).centerCrop().into(binding.ivWeatherIcon)

            binding.tvAverageTemp.text = weekly.averageTemp
            binding.tvDescription.text = weekly.description

            // 하위 RecyclerView를 초기화하지 않고, 어댑터의 데이터만 업데이트
            val clothesTemp = ClothesTemp.findByClothesTemp((weekly.minTemp + weekly.maxTemp) / 2)
            val clothsList = mutableListOf<String>()
            Log.d(TAG.TEST.label, "clothesTemp : ${clothesTemp.valueName}")

            val forestRef = Firebase.storage.reference.child("images/$clothesTemp/")
            forestRef.listAll().addOnSuccessListener { metadata ->
                for (item in metadata.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        clothsList.add(uri.toString())
                        binding.rvClothes.adapter?.notifyDataSetChanged()
                    }
                }

                // 어댑터가 아직 설정되지 않은 경우에만 어댑터를 초기화
                if (binding.rvClothes.adapter == null) {
                    binding.rvClothes.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = AverageClothesAdapter(clothsList, context)
                        visibility = View.GONE
                    }
                }
            }
            binding.ivExpand.setOnClickListener {
                if (!binding.ivExpand.isSelected) {
                    binding.rvClothes.visibility = View.VISIBLE
                } else {
                    binding.rvClothes.visibility = View.GONE
                }
                binding.ivExpand.isSelected = !binding.ivExpand.isSelected
            }
        }

    }
}