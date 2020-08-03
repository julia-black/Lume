package com.singlelab.lume.ui.creating_event

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.location.MapLocation
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.creating_event.adapter.EventImagesAdapter
import com.singlelab.lume.ui.creating_event.adapter.OnImageClickListener
import com.singlelab.lume.ui.map.MapFragment
import com.singlelab.lume.util.formatToUTC
import com.singlelab.lume.util.getBitmap
import com.singlelab.net.model.event.EventRequest
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_creating_event.*
import kotlinx.android.synthetic.main.fragment_creating_event.description
import kotlinx.android.synthetic.main.fragment_events.button_create_event
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CreatingEventFragment : BaseFragment(), CreatingEventView, OnlyForAuthFragments,
    OnActivityResultListener, OnImageClickListener {

    @Inject
    lateinit var daggerPresenter: CreatingEventPresenter

    @InjectPresenter
    lateinit var presenter: CreatingEventPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

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
        recycler_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EventImagesAdapter()
            (adapter as EventImagesAdapter).setClickListener(this@CreatingEventFragment)
        }
    }

    override fun onCompleteCreateEvent(eventUid: String) {
        Toast.makeText(context, "Ура! Вы создали событие!", Toast.LENGTH_LONG).show()
        val action = CreatingEventFragmentDirections.actionCreatingEventToEvent(eventUid)
        findNavController().navigate(action)
        findNavController().popBackStack(R.id.creating_event, true)
    }

    override fun showDateStart(dateStr: String) {
        start_date.text = dateStr
    }

    override fun showDateEnd(dateStr: String) {
        end_date.text = dateStr
    }

    override fun addImage(bitmap: Bitmap) {
        (recycler_images.adapter as EventImagesAdapter).addImage(bitmap)
    }

    override fun showCurrentCity(cityId: Int?, cityName: String?) {
        if (cityId != null && cityName != null) {
            text_city.text = cityName
        } else {
            text_city.text = getString(R.string.city)
        }
    }

    override fun showLocationName(locationName: String?) {
        if (locationName != null) {
            text_location.text = locationName
        } else {
            text_location.text = getString(R.string.choose_location)
        }
    }

    override fun showWarningOtherCity(currentCity: String) {
        Toast.makeText(
            context,
            getString(R.string.warning_other_city, currentCity),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val bitmap = result.uri.getBitmap(activity?.contentResolver)
                presenter.addImage(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onClickNewImage() {
        activity?.let { activity ->
            CropImage.activity()
                .setFixAspectRatio(true)
                .setRequestedSize(500, 500, CropImageView.RequestSizeOptions.RESIZE_FIT)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(activity)
        }
    }

    override fun onClickDeleteImage(position: Int) {
        (recycler_images.adapter as EventImagesAdapter).deleteImage(position)
        presenter.deleteImage(position)
    }

    private fun setListeners() {
        text_city.setOnClickListener {
            toChooseCity()
        }
        text_location.setOnClickListener {
            toChooseLocation()
        }
        start_date.setOnClickListener {
            showDatePicker(presenter.currentDateStart, isStart = true)
        }
        end_date.setOnClickListener {
            showDatePicker(presenter.currentDateEnd, isStart = false)
        }
        switch_online.setOnCheckedChangeListener { _, isChecked ->
            text_city.isEnabled = !isChecked
            text_location.isEnabled = !isChecked
        }
        button_create_event.setOnClickListener {
            if (validation()) {
                val minAge =
                    if (min_age.text.isNullOrEmpty()) null else min_age.text.toString().toInt()
                val maxAge =
                    if (max_age.text.isNullOrEmpty()) null else max_age.text.toString().toInt()
                val event = EventRequest(
                    name = title.text.toString(),
                    description = description.text.toString(),
                    minAge = minAge,
                    maxAge = maxAge,
                    xCoordinate = if (switch_online.isChecked) null else presenter.getLat(),
                    yCoordinate = if (switch_online.isChecked) null else presenter.getLong(),
                    startTime = presenter.currentDateStart?.time.formatToUTC(Const.DATE_FORMAT_TIME_ZONE),
                    endTime = presenter.currentDateEnd?.time.formatToUTC(Const.DATE_FORMAT_TIME_ZONE),
                    isOpenForInvitations = switch_open_event.isChecked,
                    primaryImage = presenter.getPrimaryImage(),
                    images = presenter.getImagesStr(),
                    cityId = if (switch_online.isChecked) null else presenter.cityId!!,
                    isOnline = switch_online.isChecked
                )
                presenter.createEvent(event)
            } else {
                showError(getString(R.string.enter_fields))
            }
        }
    }

    private fun toChooseLocation() {
        parentFragmentManager.setFragmentResultListener(MapFragment.REQUEST_LOCATION,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
        val action = CreatingEventFragmentDirections.actionCreatingEventToMap(
            presenter.getAddress() ?: presenter.cityName!!
        )
        findNavController().navigate(action)
    }

    private fun toChooseCity() {
        parentFragmentManager.setFragmentResultListener(
            CitiesFragment.REQUEST_CITY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
        findNavController().navigate(CreatingEventFragmentDirections.actionCreatingEventToCities())
    }

    private fun validation(): Boolean {
        when {
            title.text.isNullOrBlank() -> {
                return false
            }
            description.text.isNullOrBlank() -> {
                return false
            }
            presenter.currentDateStart == null -> {
                return false
            }
            presenter.currentDateEnd == null -> {
                return false
            }
            presenter.cityId == null || presenter.cityId!! < 0 -> {
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
                    presenter.saveCurrentDate(year, month, dayOfMonth, isStart)
                    showTimePicker(presenter.currentDateStart, isStart)
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            )
            if (isStart || presenter.currentDateStart == null) {
                picker.datePicker.minDate = Date().time
            } else {
                picker.datePicker.minDate = presenter.currentDateStart!!.timeInMillis
            }
            picker.show()
        }
    }

    private fun showTimePicker(currentDateTime: Calendar?, isStart: Boolean) {
        context?.let {
            val dateTime = currentDateTime ?: Calendar.getInstance()
            TimePickerDialog(
                it, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    presenter.saveCurrentTime(hourOfDay, minute, isStart)
                },
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            CitiesFragment.REQUEST_CITY -> {
                val city: City = result.getParcelable(CitiesFragment.RESULT_CITY) ?: return
                presenter.setCity(city)
            }
            MapFragment.REQUEST_LOCATION -> {
                val location: MapLocation =
                    result.getParcelable(MapFragment.RESULT_LOCATION) ?: return
                presenter.setMapLocation(location)
            }
        }
    }
}