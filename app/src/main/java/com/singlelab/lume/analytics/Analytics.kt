package com.singlelab.lume.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.singlelab.lume.model.analytics.AnalyticsEvent
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.EventRequest

object Analytics {

    private const val EVENT_ID = "event_id"
    private const val EVENT_NAME = "event_name"
    private const val EVENT_MIN_AGE = "min_age"
    private const val EVENT_MAX_AGE = "max_age"
    private const val EVENT_TYPES = "event_types"

    private const val COUNT_IMAGES = "count_images"
    private const val AGE = "age"
    private const val CITY_ID = "city_id"
    private const val CITY_NAME = "city_name"

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init() {
        firebaseAnalytics = Firebase.analytics
    }

    fun logCreateEvent(eventId: String, event: EventRequest) {
        val params = bundleOf(
            Pair(EVENT_ID, eventId),
            Pair(EVENT_NAME, event.name),
            Pair(EVENT_MIN_AGE, event.minAge),
            Pair(EVENT_MAX_AGE, event.maxAge),
            Pair(COUNT_IMAGES, event.images?.size ?: 0),
            Pair(EVENT_TYPES, event.types)
        )
        logFirebaseEvent(AnalyticsEvent.CREATE_EVENT.title, params.addUserParams())
    }

    fun logOpenChat() {
        logFirebaseEvent(AnalyticsEvent.OPEN_CHAT.title, bundleOf().addUserParams())
    }

    fun logSendMessage() {
        logFirebaseEvent(AnalyticsEvent.SEND_MESSAGE.title, bundleOf().addUserParams())
    }

    fun logPromoClick() {
        logFirebaseEvent(AnalyticsEvent.PROMO_CLICK.title, bundleOf().addUserParams())
    }

    private fun Bundle.addUserParams(): Bundle {
        AuthData.age?.let {
            this.putInt(AGE, it)
        }
        AuthData.cityId?.let {
            this.putInt(CITY_ID, it)
        }
        AuthData.cityName?.let {
            this.putString(CITY_NAME, it)
        }
        return this
    }

    private fun logFirebaseEvent(event: String, bundle: Bundle) {
        if (!BuildConfig.DEBUG) {
            firebaseAnalytics.logEvent(event, bundle)
        }
    }
}