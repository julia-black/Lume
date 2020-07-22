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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.data.model.consts.Const
import com.singlelab.data.model.event.Event
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.ui.creating_event.adapter.EventImagesAdapter
import com.singlelab.lume.ui.creating_event.adapter.OnImageClickListener
import com.singlelab.lume.util.getBitmap
import com.singlelab.lume.util.parseToString
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

    private fun setListeners() {
        start_date.setOnClickListener {
            showDatePicker(presenter.currentDateStart, isStart = true)
        }
        end_date.setOnClickListener {
            showDatePicker(presenter.currentDateEnd, isStart = false)
        }
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
                    startTime = presenter.currentDateStart?.time.parseToString(Const.DATE_FORMAT_TIME_ZONE),
                    endTime = presenter.currentDateEnd?.time.parseToString(Const.DATE_FORMAT_TIME_ZONE)
                )
                presenter.createEvent(event)
            } else {
                showError(getString(R.string.enter_fields))
            }
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
            presenter.currentDateStart == null -> {
                return false
            }
            presenter.currentDateEnd == null -> {
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

    override fun onClickNewImage() {
        activity?.let { activity ->
            CropImage.activity()
                .setFixAspectRatio(true)
                .setRequestedSize(300, 300, CropImageView.RequestSizeOptions.RESIZE_FIT)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(activity)
        }
    }

    override fun onClickDeleteImage(position: Int) {
        (recycler_images.adapter as EventImagesAdapter).deleteImage(position)
        presenter.deleteImage(position)
    }
}