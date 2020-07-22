package com.singlelab.lume.ui.creating_event

import android.graphics.Bitmap
import com.singlelab.net.exceptions.ApiException
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractor
import com.singlelab.lume.util.parseToString
import com.singlelab.net.model.event.EventRequest
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class CreatingEventPresenter @Inject constructor(
    private val interactor: CreatingEventInteractor,
    preferences: Preferences?
) : BasePresenter<CreatingEventView>(preferences, interactor as BaseInteractor) {

    var currentDateStart: Calendar? = null

    var currentDateEnd: Calendar? = null

    var images: MutableList<Bitmap> = mutableListOf()

    fun createEvent(event: EventRequest) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val eventUid = interactor.createEvent(event)?.eventUid
                runOnMainThread {
                    viewState.showLoading(false)
                    eventUid?.let {
                        viewState.onCompleteCreateEvent(it)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun saveCurrentDate(year: Int, month: Int, day: Int, isStart: Boolean) {
        if (isStart) {
            if (currentDateStart == null) {
                currentDateStart = Calendar.getInstance()
            }
            currentDateStart!!.set(Calendar.YEAR, year)
            currentDateStart!!.set(Calendar.MONTH, month)
            currentDateStart!!.set(Calendar.DAY_OF_MONTH, day)
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.YEAR, year)
            currentDateEnd!!.set(Calendar.MONTH, month)
            currentDateEnd!!.set(Calendar.DAY_OF_MONTH, day)
        }
    }

    fun saveCurrentTime(hours: Int, minutes: Int, isStart: Boolean) {
        if (isStart) {
            if (currentDateStart == null) {
                currentDateStart = Calendar.getInstance()
            }
            currentDateStart!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateStart!!.set(Calendar.MINUTE, minutes)
            viewState.showDateStart(currentDateStart!!.parseToString(Const.DATE_FORMAT_OUTPUT))
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateEnd!!.set(Calendar.MINUTE, minutes)
            viewState.showDateEnd(currentDateEnd!!.parseToString(Const.DATE_FORMAT_OUTPUT))
        }
    }

    fun addImage(bitmap: Bitmap?) {
        bitmap?.let {
            images.add(it)
            viewState.addImage(bitmap)
        }
    }

    fun deleteImage(position: Int) {
        images.removeAt(position)
    }
}