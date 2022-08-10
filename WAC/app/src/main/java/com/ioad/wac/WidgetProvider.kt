package com.ioad.wac

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ioad.wac.activity.MainBoardActivity2
import com.ioad.wac.service.MyRemoteViewsService

class WidgetProvider : AppWidgetProvider() {

    private val MY_ACTION = "android.action.MY_ACTION"

//    private fun setMyAction(context: Context?):PendingIntent {
//        val intent = Intent(context, MainBoardActivity2::class.java)
//        return PendingIntent.getActivity(context, 0, intent, 0)
//    }
//
//    @SuppressLint("RemoteViewLayout")
//    private fun addView(context: Context?):RemoteViews {
//        val views = RemoteViews(context?.packageName, R.layout.widget)
//        views.setOnClickPendingIntent(R.id.tv_widget_temp, setMyAction(context))
//        return views
//    }


    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
//            val views:RemoteViews = addView(context)
//            appWidgetManager?.updateAppWidget(it, views)
//            updateAppWidget(context, appWidgetManager, appWidgetId);
            val serviceIntent = Intent(context, MyRemoteViewsService::class.java)
            val widget = RemoteViews(context?.packageName, R.layout.widget)
            widget.setRemoteAdapter(R.id.lv_widget, serviceIntent)

            appWidgetManager?.updateAppWidget(appWidgetIds, widget)
        }


//        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }


}