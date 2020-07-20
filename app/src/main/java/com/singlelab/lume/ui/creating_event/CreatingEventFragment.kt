package com.singlelab.lume.ui.creating_event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.singlelab.data.model.consts.Const
import com.singlelab.data.model.event.Event
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.util.parseToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_creating_event.*
import kotlinx.android.synthetic.main.fragment_events.button_create_event
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CreatingEventFragment : BaseFragment(), CreatingEventView, OnlyForAuthFragments {

    @Inject
    lateinit var daggerPresenter: CreatingEventPresenter

    @InjectPresenter
    lateinit var presenter: CreatingEventPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var currentDateStart: Calendar? = null

    private var currentDateEnd: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_creating_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_new_event)
        setListeners()
    }

    override fun onCompleteCreateEvent(eventUid: String) {
        //todo переход в детали
        Toast.makeText(context, "Ура! Вы создали событие!", Toast.LENGTH_LONG).show()
        Navigation.createNavigateOnClickListener(R.id.action_creating_event_to_events).onClick(view)
    }


    private fun setListeners() {
        button_create_event.setOnClickListener {
            if (validation()) {
                val minAge =
                    if (min_age.text.isNullOrEmpty()) null else min_age.text.toString().toInt()
                val maxAge =
                    if (max_age.text.isNullOrEmpty()) null else max_age.text.toString().toInt()
                val event = Event(
                    name = title.text.toString(),
                    description = description.text.toString(),
                    minAge = minAge,
                    maxAge = maxAge,
                    xCoordinate = 51.5819596F,
                    yCoordinate = 46.0621339F,
                    startTime = currentDateStart?.time.parseToString(Const.DATE_FORMAT_TIME_ZONE),
                    endTime = currentDateEnd?.time.parseToString(Const.DATE_FORMAT_TIME_ZONE)
                )
                presenter.createEvent(event)
            } else {
                showError(getString(R.string.enter_fields))
            }
        }
        start_date.setOnClickListener {
            showDatePicker(currentDateStart, isStart = true)
        }
        end_date.setOnClickListener {
            showDatePicker(currentDateEnd, isStart = false)
        }
    }

    private fun validation(): Boolean {
        when {
            title.text.isNullOrBlank() -> {
                return false
            }
            description.text.isNullOrBlank() -> {
                return false
            }
            currentDateStart == null -> {
                return false
            }
            currentDateEnd == null -> {
                return false
            }
        }
        return true
    }

    private fun showDatePicker(currentDateTime: Calendar?, isStart: Boolean) {
        context?.let {
            val date = currentDateTime ?: Calendar.getInstance()
            val picker = DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    saveCurrentDate(year, month, dayOfMonth, isStart)
                    showTimePicker(currentDateStart, isStart)
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            )
            if (isStart || currentDateStart == null) {
                picker.datePicker.minDate = Date().time
            } else {
                picker.datePicker.minDate = currentDateStart!!.timeInMillis
            }
            picker.show()
        }
    }

    private fun showTimePicker(currentDateTime: Calendar?, isStart: Boolean) {
        context?.let {
            val dateTime = currentDateTime ?: Calendar.getInstance()
            TimePickerDialog(
                it, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    saveCurrentTime(hourOfDay, minute, isStart)
                },
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun saveCurrentDate(year: Int, month: Int, day: Int, isStart: Boolean) {
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

    private fun saveCurrentTime(hours: Int, minutes: Int, isStart: Boolean) {
        if (isStart) {
            if (currentDateStart == null) {
                currentDateStart = Calendar.getInstance()
            }
            currentDateStart!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateStart!!.set(Calendar.MINUTE, minutes)
            start_date.text = currentDateStart!!.parseToString(Const.DATE_FORMAT_OUTPUT)
        } else {
            if (currentDateEnd == null) {
                currentDateEnd = Calendar.getInstance()
            }
            currentDateEnd!!.set(Calendar.HOUR_OF_DAY, hours)
            currentDateEnd!!.set(Calendar.MINUTE, minutes)
            end_date.text = currentDateEnd!!.parseToString(Const.DATE_FORMAT_OUTPUT)
        }
    }
}