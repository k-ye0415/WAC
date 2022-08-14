package com.ioad.wac

import androidx.recyclerview.widget.DiffUtil
import com.ioad.wac.model.Locations

class DiffUtilCallback(
    private val oldList:ArrayList<Locations>,
    private val newList:ArrayList<Locations>
):DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition)
    }
}