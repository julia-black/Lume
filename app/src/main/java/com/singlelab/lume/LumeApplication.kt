package com.singlelab.lume

import android.app.Application
import android.content.Context
import com.singlelab.lume.model.Const
import com.singlelab.lume.pref.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LumeApplication : Application() {
    companion object {
        var preferences: Preferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(getSharedPreferences(Const.PREF, Context.MODE_PRIVATE))
    }
}