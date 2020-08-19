package com.singlelab.lume.ui.filters

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.EventType
import com.singlelab.lume.ui.cities.CitiesFragment
import dagger.hilt.android.AndroidEntryPoint
import io.apptik.widget.MultiSlider
import io.apptik.widget.MultiSlider.Thumb
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.view_grid_emoji.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class FilterFragment : BaseFragment(), FilterView, OnPermissionListener {

    companion object {
        const val REQUEST_FILTER = "REQUEST_FILTER"
        const val RESULT_FILTER = "RESULT_FILTER"
        const val FAR_DISTANCE = 2
    }

    @Inject
    lateinit var daggerPresenter: FilterPresenter

    @InjectPresenter
    lateinit var presenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private lateinit var locationClient: FusedLocationProviderClient

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
            presenter.filterEvent = FilterFragmentArgs.fromBundle(it).filterEvent
            presenter.filterPerson = FilterFragmentArgs.fromBundle(it).filterPerson
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

    private fun showFilters(isEvent: Boolean) {
        if (isEvent) {
            emoji_grid.visibility = View.VISIBLE
            title_types.visibility = View.VISIBLE
            choose_types.visibility = View.VISIBLE
            seek_bar_distance.visibility = View.VISIBLE
            text_age.visibility = View.GONE
            seek_bar_age.visibility = View.GONE
            presenter.filterEvent?.let {
                seek_bar_distance.progress = it.distance.id
                text_distance.text = it.distance.title
                if (it.cityName != null) {
                    text_city.text = it.cityName
                } else {
                    text_city.setText(R.string.any_city)
                }
                switch_online.isChecked = it.isOnlyOnline
                switch_not_online.isChecked = it.isExceptOnline

            }
        } else {
            text_age.visibility = View.VISIBLE
            seek_bar_age.visibility = View.VISIBLE
            choose_types.visibility = View.GONE
            emoji_grid.visibility = View.GONE
            title_types.visibility = View.GONE
            seek_bar_distance.visibility = View.GONE
            text_distance.visibility = View.GONE
            switch_online.visibility = View.GONE
            switch_not_online.visibility = View.GONE
            presenter.filterPerson?.let {
                seek_bar_age.min = Const.MIN_AGE
                seek_bar_age.max = Const.MAX_AGE
                if (it.minAge == Const.MIN_AGE && it.maxAge == Const.MAX_AGE) {
                    text_age.setText(R.string.any_age)
                } else {
                    seek_bar_age.getThumb(0).value = it.minAge
                    seek_bar_age.getThumb(1).value = it.maxAge
                    if (it.minAge == it.maxAge) {
                        text_age.text = getString(R.string.age_exact, it.minAge)
                    } else {
                        text_age.text = getString(R.string.age_from_to, it.minAge, it.maxAge)
                    }
                }
            }
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
        switch_online.setOnCheckedChangeListener { _, isChecked ->
            presenter.filterEvent?.isOnlyOnline = isChecked
            if (isChecked) {
                text_city.isEnabled = false
                seek_bar_distance.isEnabled = false
                text_distance.isEnabled = false
                switch_not_online.isChecked = false
                presenter.filterEvent?.isExceptOnline = false
            } else {
                text_city.isEnabled = true
                seek_bar_distance.isEnabled = true
                text_distance.isEnabled = true
            }
        }
        switch_not_online.setOnCheckedChangeListener { _, isChecked ->
            presenter.filterEvent?.isExceptOnline = isChecked
            if (isChecked) {
                switch_online.isChecked = false
                presenter.filterEvent?.isOnlyOnline = false
            }
        }
        seek_bar_distance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.changeDistance(progress)
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

        seek_bar_age.setOnThumbValueChangeListener { _: MultiSlider?, _: Thumb?, thumbIndex: Int, value: Int ->
            if (thumbIndex == 0) {
                presenter.filterPerson?.minAge = value
                if (value == presenter.filterPerson?.maxAge) {
                    text_age.text = getString(R.string.age_exact, value)
                } else {
                    text_age.text = getString(
                        R.string.age_from_to,
                        presenter.filterPerson?.minAge,
                        presenter.filterPerson?.maxAge
                    )
                }
            } else {
                presenter.filterPerson?.maxAge = value
                if (value == presenter.filterPerson?.minAge) {
                    text_age.text = getString(R.string.age_exact, value)
                } else {
                    text_age.text = getString(
                        R.string.age_from_to,
                        presenter.filterPerson?.minAge,
                        presenter.filterPerson?.maxAge
                    )
                }
            }
            if (seek_bar_age.getThumb(0).value == Const.MIN_AGE && seek_bar_age.getThumb(1).value == Const.MAX_AGE) {
                text_age.setText(R.string.any_age)
            }
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

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let { context ->
            locationClient = LocationServices.getFusedLocationProviderClient(context)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        onErrorGeo()
                    } else {
                        presenter.setUserLocation(location.longitude, location.latitude)
                    }
                }
        }
    }

    private fun onErrorGeo() {
        showError(getString(R.string.permission_location_denied))
        presenter.changeDistance(FAR_DISTANCE)
    }

    private fun toChooseCity() {
        val action = FilterFragmentDirections.actionFiltersToCities()
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
}