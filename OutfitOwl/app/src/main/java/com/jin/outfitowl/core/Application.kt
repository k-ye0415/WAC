package com.jin.outfitowl.core

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.jin.outfitowl.util.TAG
import com.jin.outfitowl.viewModel.ViewModel
import database.AppDatabase

class Application : android.app.Application() {
    init {
        Log.e(TAG.TEST.label, "Application init")
        instance = this
        Log.e(TAG.TEST.label, "Application init and ${if (instance == null) "null" else "not null"}")
    }

    companion object {
        lateinit var instance: Application
    }

    val database by lazy { AppDatabase.getInstance(applicationContext) }
    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG.TEST.label, "Application onCreate()")
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(ViewModel::class.java)
    }
}