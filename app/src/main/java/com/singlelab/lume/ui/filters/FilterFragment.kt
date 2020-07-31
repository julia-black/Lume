package com.singlelab.lume.ui.filters

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.event.EventType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filters.*
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
        activity?.title = getString(R.string.title_filters)
        arguments?.let {
            presenter.filterEvent = FilterFragmentArgs.fromBundle(it).filterEvent
            //todo будет еще filterPerson
        }
        showFilters(presenter.isEvent())
        setListeners()
    }

    override fun showDistance(distance: Distance) {
        text_distance.text = distance.title
        seek_bar_distance.progress = distance.id
    }

    override fun showCity(cityName: String) {
        text_city.text = cityName
    }

    override fun onLocationPermissionGranted() {
        getLocation()
    }

    override fun onLocationPermissionDenied() {
        onErrorGeo()
    }

    private fun showFilters(isEvent: Boolean) {
        if (isEvent) {
            chip_groups.visibility = View.VISIBLE
            seek_bar_distance.visibility = View.VISIBLE
            presenter.filterEvent?.let {
                seek_bar_distance.progress = it.distance.id
                text_distance.text = it.distance.title
                text_city.text = it.cityName
                if (it.selectedTypes.contains(EventType.PARTY)) {
                    chip_party.isChecked = true
                }
                if (it.selectedTypes.contains(EventType.BOOZE)) {
                    chip_booze.isChecked = true
                }
                switch_online.isChecked = it.isOnlyOnline
                switch_not_online.isChecked = it.isExceptOnline
            }
        } else {
            chip_groups.visibility = View.GONE
            seek_bar_distance.visibility = View.GONE
            text_distance.visibility = View.GONE
        }
    }

    private fun setListeners() {
        chip_party.setOnCheckedChangeListener { _, isChecked ->
            presenter.setCheckedEventType(EventType.PARTY, isChecked)
        }
        chip_booze.setOnCheckedChangeListener { _, isChecked ->
            presenter.setCheckedEventType(EventType.BOOZE, isChecked)
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
        button_apply.setOnClickListener {
            presenter.filterEvent?.let {
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
        Toast.makeText(
            context,
            getString(R.string.permission_location_denied),
            Toast.LENGTH_LONG
        ).show()
        presenter.changeDistance(FAR_DISTANCE)
    }

    private fun toChooseCity() {
        findNavController().navigate(FilterFragmentDirections.actionFiltersToCities())
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CitiesFragment.REQUEST_CITY) {
            val city: City = result.getParcelable(CitiesFragment.RESULT_CITY) ?: return
            presenter.setCity(city)
        }
    }

    private fun applyFilter(filterEvent: FilterEvent) {
        parentFragmentManager.setFragmentResult(
            REQUEST_FILTER,
            bundleOf(RESULT_FILTER to filterEvent)
        )
        parentFragmentManager.popBackStack()
    }
}