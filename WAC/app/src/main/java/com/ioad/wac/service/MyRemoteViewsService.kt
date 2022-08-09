package com.ioad.wac.service

import android.content.Intent

import android.widget.RemoteViewsService
import com.ioad.wac.adapter.RemoteViewsFactory


class MyRemoteViewsService : RemoteViewsService() {
    //필수 오버라이드 함수 : RemoteViewsFactory를 반환한다.
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val remoteViewsFactory = com.ioad.wac.adapter.RemoteViewsFactory(this)
        return remoteViewsFactory
    }
}