package com.ioad.wac.adapter

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.ioad.wac.R
import com.ioad.wac.model.Widget

class RemoteViewsFactory(context: Context):RemoteViewsService.RemoteViewsFactory {

    var context:Context? = null
    var arrayList: ArrayList<Widget>? = null
    init {
        this.context = context
    }

//    fun RemoteViewsFactory(context: Context) {
//        this.context = context
//    }

    fun setData() {
        arrayList = ArrayList()
        arrayList!!.add(Widget(1, "First"))
        arrayList!!.add(Widget(2, "Second"))
        arrayList!!.add(Widget(3, "Third"))
        arrayList!!.add(Widget(4, "Fourth"))
        arrayList!!.add(Widget(5, "Fifth"))
    }


    override fun onCreate() {
        setData()
    }

    override fun onDataSetChanged() {
        setData()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return arrayList!!.size
    }


    override fun getViewAt(position: Int): RemoteViews {
        val listViewWidget = RemoteViews(context?.packageName, R.layout.widget_item)
        listViewWidget.setTextViewText(R.id.tv_widget_item, arrayList?.get(position)?.content)

        val dataIntent = Intent()
        dataIntent.putExtra("item_id", arrayList?.get(position)?.id)
        dataIntent.putExtra("item_data", arrayList?.get(position)?.content)
        listViewWidget.setOnClickFillInIntent(R.id.tv_widget_item, dataIntent)

        return listViewWidget

    }

    override fun getLoadingView(): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }


}