package com.singlelab.lume.ui.filters.event

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.EventType
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.view.range_picker.DateRangePicker
import com.singlelab.lume.util.toDateFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.view_grid_emoji.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class FilterFragment : BaseFragment(),
    FilterView, OnPermissionListener {

    @Inject
    lateinit var daggerPresenter: FilterPresenter

    @InjectPresenter
    lateinit var presenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private lateinit var locationClient: FusedLocationProviderClient

    private var geoIsAvailable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.filterEvent = FilterFragmentArgs.fromBundle(
                it
            ).filterEvent
        }
        showFilters(presenter.isEvent())
        setListeners()
    }

    override fun showDistance(distance: Distance) {
        text_distance.text = distance.title
        seek_bar_distance.progress = distance.id
    }

    override fun showCity(cityName: String?) {
        text_city.text = cityName ?: getString(R.string.any_city)
    }

    override fun onLocationPermissionGranted() {
        getLocation()
    }

    override fun onLocationPermissionDenied() {
        onErrorGeo()
    }

    override fun onContactsPermissionGranted() {
    }

    override fun onContactsPermissionDenied() {
    }

    override fun onWriteExternalPermissionGranted() {
    }

    override fun onWriteExternalPermissionDenied() {
    }

    private fun showFilters(isEvent: Boolean) {
        if (isEvent) {
            emoji_grid.visibility = View.VISIBLE
            title_types.visibility = View.VISIBLE
            choose_types.visibility = View.VISIBLE
            seek_bar_distance.visibility = View.VISIBLE
            text_choose_date.visibility = View.VISIBLE
            button_choose_date.visibility = View.VISIBLE
            checkbox_online.setText(getString(R.string.events_is_online))
            checkbox_not_online.setText(getString(R.string.events_not_online))
            presenter.filterEvent?.let {
                seek_bar_distance.progress = it.distance.id
                text_distance.text = it.distance.title
                if (it.cityName != null) {
                    text_city.text = it.cityName
                } else {
                    text_city.setText(R.string.any_city)
                }
                checkbox_online.setChecked(it.isOnlyOnline)
                checkbox_not_online.setChecked(it.isExceptOnline)
                showDate(it.minimalStartTime, it.maximalEndTime)
            }
            showLocation(!checkbox_online.getChecked())
        } else {
            text_choose_date.visibility = View.GONE
            choose_types.visibility = View.GONE
            emoji_grid.visibility = View.GONE
            title_types.visibility = View.GONE
            seek_bar_distance.visibility = View.GONE
            text_distance.visibility = View.GONE
            checkbox_online.visibility = View.GONE
            checkbox_not_online.visibility = View.GONE
            button_choose_date.visibility = View.GONE
            if (presenter.filterPerson?.cityName != null) {
                text_city.text = presenter.filterPerson!!.cityName
            } else {
                text_city.setText(R.string.any_city)
            }
        }
    }

    override fun showTypes(selectedTypes: List<EventType>) {
        if (selectedTypes.isEmpty() || selectedTypes.size == EventType.values().size) {
            title_types.text = getString(R.string.selected_all)
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

    private fun setListeners() {
        emoji_grid.children.forEachIndexed { index, view ->
            view.setOnClickListener {
                presenter.onClickType(index)
            }
        }
        checkbox_online.setListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            presenter.filterEvent?.isOnlyOnline = isChecked
            showLocation(!isChecked)
        })
        checkbox_not_online.setListener(
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                presenter.filterEvent?.isExceptOnline = isChecked
                if (isChecked) {
                    checkbox_online.setChecked(false)
                    presenter.filterEvent?.isOnlyOnline = false
                }
            })
        seek_bar_distance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.changeDistance(if (geoIsAvailable) progress else Distance.FAR.id)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                (activity as MainActivity).checkLocationPermission()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        text_city.setOnClickListener {
            toChooseCity()
        }

//        seek_bar_age.setOnThumbValueChangeListener { _: MultiSlider?, _: Thumb?, thumbIndex: Int, value: Int ->
//            if (thumbIndex == 0) {
//                presenter.filterPerson?.minAge = value
//                if (value == presenter.filterPerson?.maxAge) {
//                    text_age.text = getString(R.string.age_exact, value)
//                } else {
//                    text_age.text = getString(
//                        R.string.age_from_to,
//                        presenter.filterPerson?.minAge,
//                        presenter.filterPerson?.maxAge
//                    )
//                }
//            } else {
//                presenter.filterPerson?.maxAge = value
//                if (value == presenter.filterPerson?.minAge) {
//                    text_age.text = getString(R.string.age_exact, value)
//                } else {
//                    text_age.text = getString(
//                        R.string.age_from_to,
//                        presenter.filterPerson?.minAge,
//                        presenter.filterPerson?.maxAge
//                    )
//                }
//            }
//            if (seek_bar_age.getThumb(0).value == Const.MIN_AGE && seek_bar_age.getThumb(1).value == Const.MAX_AGE) {
//                text_age.setText(R.string.any_age)
//            }
//        }
        button_choose_date.setOnClickListener {
            showDateRangePicker()
        }
        button_clear_date.setOnClickListener {
            presenter.setDate(null, null)
        }
        button_apply.setOnClickListener {
            presenter.filterEvent?.let {
                applyFilter(it)
            }
            presenter.filterPerson?.let {
                applyFilter(it)
            }
        }
        parentFragmentManager.setFragmentResultListener(
            CitiesFragment.REQUEST_CITY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    override fun showDate(firstDate: Long?, secondDate: Long?) {
        if (firstDate != null && secondDate != null) {
            text_choose_date.text = getString(
                R.string.range,
                firstDate.toDateFormat(Const.DATE_FORMAT_SHORT_DATE),
                secondDate.toDateFormat(Const.DATE_FORMAT_SHORT_DATE)
            )
            button_clear_date.visibility = View.VISIBLE
        } else {
            text_choose_date.text = getString(R.string.choose_date)
            button_clear_date.visibility = View.GONE
        }
    }

    private fun showDateRangePicker() {
        activity?.supportFragmentManager?.let { manager ->
            val rangePicker = DateRangePicker(manager, View.OnClickListener {
                hideKeyboard()
            },
                MaterialPickerOnPositiveButtonClickListener {
                    hideKeyboard()
                    presenter.setDate(it.first, it.second)
                })
            rangePicker.show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let { context ->
            locationClient = LocationServices.getFusedLocationProviderClient(context)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        val lastLocation = getLastKnownLocation()
                        if (lastLocation == null) {
                            if (isEnabledGeoLocation()) {
                                showAttentionUnavailableGeo()
                            } else {
                                toLocationSettings()
                            }
                        } else {
                            geoIsAvailable = true
                            presenter.setUserLocation(lastLocation.longitude, lastLocation.latitude)
                        }
                    } else {
                        geoIsAvailable = true
                        presenter.setUserLocation(location.longitude, location.latitude)
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = manager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = manager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun showAttentionUnavailableGeo() {
        geoIsAvailable = false
        showError(getString(R.string.on_geolocation))
        presenter.changeDistance(FAR_DISTANCE)
    }

    private fun isEnabledGeoLocation(): Boolean {
        var gpsState: Int? = null
        try {
            gpsState =
                Settings.Secure.getInt(activity?.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return gpsState != null
    }

    private fun toLocationSettings() {
        val onGPS = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(onGPS)
    }

    private fun onErrorGeo() {
        geoIsAvailable = false
        showError(getString(R.string.permission_location_denied))
        presenter.changeDistance(FAR_DISTANCE)
    }

    private fun toChooseCity() {
        val action =
            FilterFragmentDirections.actionFiltersToCities()
        action.containAnyCity = true
        findNavController().navigate(action)
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CitiesFragment.REQUEST_CITY) {
            val city: City? = result.getParcelable(CitiesFragment.RESULT_CITY)
            presenter.setCity(city)
        }
    }

    private fun applyFilter(filter: Parcelable) {
        parentFragmentManager.setFragmentResult(
            REQUEST_FILTER,
            bundleOf(RESULT_FILTER to filter)
        )
        parentFragmentManager.popBackStack()
    }

    private fun showLocation(isShow: Boolean) {
        if (isShow) {
            text_city.isEnabled = true
            seek_bar_distance.isEnabled = true
            text_distance.isEnabled = true
        } else {
            text_city.isEnabled = false
            seek_bar_distance.isEnabled = false
            text_distance.isEnabled = false
            checkbox_not_online.setChecked(false)
            presenter.filterEvent?.isExceptOnline = false
        }
    }

    companion object {
        const val REQUEST_FILTER = "REQUEST_FILTER"
        const val RESULT_FILTER = "RESULT_FILTER"
        const val FAR_DISTANCE = 2
    }
}