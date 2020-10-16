package com.singlelab.lume

import android.app.Application
import android.content.Context
import com.singlelab.lume.model.Const
import com.singlelab.lume.pref.Preferences
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LumeApplication : Application() {
    companion object {
        var preferences: Preferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        preferences = Preferences(getSharedPreferences(Const.PREF, Context.MODE_PRIVATE))
        initAppmetrica()
    }

    private fun initAppmetrica() {
        val config = YandexMetricaConfig.newConfigBuilder(getString(R.string.appmetrica_key)).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }
}