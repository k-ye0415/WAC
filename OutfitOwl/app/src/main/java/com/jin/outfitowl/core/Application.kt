package com.jin.outfitowl.core

class Application : android.app.Application() {

    init {
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: Application
    }
}