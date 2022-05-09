package com.singlelab.lume.ui.creating_event

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.os.bundleOf
import androidx.core.text.toSpannable
import androidx.core.view.children
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.Const.SELECT_IMAGE_REQUEST_CODE
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.EventType
import com.singlelab.lume.model.location.MapLocation
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.model.view.ValidationError
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.map.MapFragment
import com.singlelab.lume.ui.view.image.ImageAdapter
import com.singlelab.lume.ui.view.image.OnImageClickListener
import com.singlelab.lume.util.formatToUTC
import com.singlelab.lume.util.getBitmap
import com.singlelab.lume.util.hightlight
import com.singlelab.net.model.event.EventRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_creating_event.*
import kotlinx.android.synthetic.main.view_grid_emoji.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CreatingEventFragment : BaseFragment(), CreatingEventView, OnlyForAuthFragments,
    OnActivityResultListener,
    OnImageClickListener {

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
        initViews()
        setListeners()
        recycler_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ImageAdapter()
            (adapter as ImageAdapter).setClickListener(this@CreatingEventFragment)
        }
    }

    override fun onCompleteCreateEvent(eventUid: String) {
        showSnackbar(getString(R.string.you_create_event), ToastType.SUCCESS)
        parentFragmentManager.setFragmentResult(
            REQUEST_CREATING_EVENT, bundleOf(
                RESULT_CREATING_EVENT to eventUid
            )
        )
        parentFragmentManager.popBackStack()
    }

    override fun showDateStart(dateStr: String) {
        val timeText = getString(R.string.date_start_title, dateStr)
        start_date.text = timeText.toSpannable().hightlight(
            requireContext(),
            timeText.length - dateStr.length,
            timeText.length
        )
    }

    override fun showDateEnd(dateStr: String) {
        val timeText = getString(R.string.date_end_title, dateStr)
        end_date.text = timeText.toSpannable().hightlight(
            requireContext(),
            timeText.length - dateStr.length,
            timeText.length
        )
    }

    override fun addImage(bitmap: Bitmap) {
        (recycler_images.adapter as ImageAdapter).addImage(bitmap)
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
        showSnackbar(getString(R.string.warning_other_city, currentCity))
    }

    override fun showImages(images: MutableList<Bitmap>) {
        (recycler_images.adapter as ImageAdapter).setData(images)
    }

    override fun showTypes(selectedTypes: MutableList<EventType>) {
        if (selectedTypes.isEmpty() || selectedTypes.size == EventType.values().size) {
            title_types.text = getString(R.string.selected_nothing)
        } else {
            var text = getString(R.string.selected_types)
            selectedTypes.forEachIndexed { index, eventType ->
                text = "$text ${getString(eventType.titleRes)}"
                if (selectedTypes.size > 1 && index < selectedTypes.size - 1) {
                    text = "$text,"
                }
            }
            title_types.text = text
        }
        emoji_grid.children.forEachIndexed { index, view ->
            if (selectedTypes.find { it.id == index } != null) {
                view.alpha = 1f
            } else {
                view.alpha = 0.2f
            }
        }
    }

    override fun showLoadingImages(idx: Int, size: Int) {
        super.showLoadingText(getString(R.string.loading_images, idx, size))
    }

    override fun showTitle(title: String) {
        layout_title.setText(title)
    }

    override fun showDescription(description: String) {
        layout_description.setText(description)
    }

    override fun showClosedEvent(checked: Boolean) {
        checkbox_closed_event.setChecked(checked)
    }

    override fun showOnlineEvent(checked: Boolean) {
        checkbox_online.setChecked(checked)
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(
                requestCode,
                resultCode,
                data,
                SELECT_IMAGE_REQUEST_CODE
            )
        ) {
            val images = ImagePicker.getImages(data)
                .mapNotNull { it.uri.getBitmap(activity?.contentResolver) }
            if (resultCode == Activity.RESULT_OK && images.isNotEmpty()) {
                presenter.addImages(images)
            } else {
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun onClickNewImage() {
        onClickAddImages()
    }

    override fun onClickImage(position: Int) {
        showListDialog(
            getString(R.string.choose_action),
            arrayOf(getString(R.string.set_main), getString(R.string.remove_image)),
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> presenter.setMainImage(position)
                    1 -> onClickDeleteImage(position)
                }
            }
        )
    }

    override fun showLoading(isShow: Boolean, withoutBackground: Boolean) {
        super.showLoading(isShow, withoutBackground)
        button_create_event.isEnabled = !isShow
    }

    private fun initViews() {
        layout_title.apply {
            setHint(getString(R.string.title))
            setSingleLine()
            setMaxLength(35)
            setWarning(getString(R.string.max_length, 35))
        }
        layout_description.apply {
            setHint(getString(R.string.description))
            setLines(5)
            enableLineBreaks()
            setMaxLines(5)
            setMaxLength(428)
            setWarning(getString(R.string.max_length, 428))
        }
        checkbox_online.setText(getString(R.string.event_is_online))
        checkbox_closed_event.setText(getString(R.string.event_is_closed))
        checkbox_age.setText(getString(R.string.enable_age))
    }

    private fun setListeners() {
        text_city.setOnClickListener {
            saveInputs()
            toChooseCity()
        }
        text_location.setOnClickListener {
            saveInputs()
            toChooseLocation()
        }
        start_date.setOnClickListener {
            showDatePicker(presenter.currentDateStart, isStart = true)
        }
        end_date.setOnClickListener {
            showDatePicker(presenter.currentDateEnd, isStart = false)
        }
        checkbox_online.setListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            text_city.isEnabled = !isChecked
            text_city.alpha = if (isChecked) 0.3f else 1.0f
            text_location.isEnabled = !isChecked
            text_location.alpha = if (isChecked) 0.3f else 1.0f
        })
        checkbox_age.setListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            min_age.isEnabled = isChecked
            text_min_age.alpha = if (isChecked) 1.0f else 0.3f
            text_min_age.isEnabled = isChecked
            max_age.isEnabled = isChecked
            text_max_age.alpha = if (isChecked) 1.0f else 0.3f
            text_max_age.isEnabled = isChecked
        })
        text_min_age.setOnClickListener {
            min_age.requestFocus()
            showKeyboard()
        }
        text_max_age.setOnClickListener {
            max_age.requestFocus()
            showKeyboard()
        }
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        setGridLayoutListeners()
        button_create_event.setOnClickListener {
            val validationResult = validation()
            if (validationResult == null) {
                showLoading(true)

                val minAge = if (!checkbox_age.getChecked() || min_age.text.isNullOrEmpty()) null
                else min_age.text.toString().toInt()
                val maxAge = if (!checkbox_age.getChecked() || max_age.text.isNullOrEmpty()) null
                else max_age.text.toString().toInt()

                val event = EventRequest(
                    name = layout_title.getText(),
                    description = layout_description.getText(),
                    minAge = minAge,
                    maxAge = maxAge,
                    xCoordinate = if (checkbox_online.getChecked()) null else presenter.getLat(),
                    yCoordinate = if (checkbox_online.getChecked()) null else presenter.getLong(),
                    startTime = presenter.currentDateStart?.time.formatToUTC(Const.DATE_FORMAT_TIME_ZONE),
                    endTime = presenter.currentDateEnd?.time.formatToUTC(Const.DATE_FORMAT_TIME_ZONE),
                    isOpenForInvitations = !checkbox_closed_event.getChecked(),
                    primaryImage = presenter.getPrimaryImage(),
                    types = presenter.getTypes().map { it.id }.toTypedArray(),
                    cityId = if (checkbox_online.getChecked()) null else presenter.cityId!!,
                    isOnline = checkbox_online.getChecked()
                )
                presenter.createEvent(event)
            } else {
                showError(getString(validationResult.titleResId))
            }
        }
    }

    private fun saveInputs() {
        presenter.setTitle(layout_title.getText())
        presenter.setDescription(layout_description.getText())
        presenter.setOnlineEvent(checkbox_online.getChecked())
        presenter.setClosedEvent(checkbox_closed_event.getChecked())
    }

    private fun onClickDeleteImage(position: Int) {
        presenter.deleteImage(position)
    }

    private fun setGridLayoutListeners() {
        emoji_grid.children.forEachIndexed { index, view ->
            view.setOnClickListener {
                presenter.onClickType(index)
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

    private fun validation(): ValidationError? {
        when {
            layout_title.getText().isBlank() -> {
                return ValidationError.EMPTY_TITLE
            }
            layout_description.getText().isBlank() -> {
                return ValidationError.EMPTY_DESCRIPTION_EVENT
            }
            presenter.currentDateStart == null -> {
                return ValidationError.EMPTY_START_TIME
            }
            presenter.currentDateEnd == null -> {
                return ValidationError.EMPTY_END_TIME
            }
            presenter.cityId == null || presenter.cityId!! < 0 -> {
                return ValidationError.EMPTY_CITY
            }
        }
        return null
    }

    private fun showDatePicker(currentDateTime: Calendar?, isStart: Boolean) {
        context?.let {
            val date = currentDateTime ?: Calendar.getInstance()
            val picker = DatePickerDialog(
                it, { _, year, month, dayOfMonth ->
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

    companion object {
        const val REQUEST_CREATING_EVENT = "CREATING_EVENT_REQUEST"
        const val RESULT_CREATING_EVENT = "CREATING_EVENT_RESULT"
    }
}