package com.singlelab.lume.ui.map

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.location.MapLocation
import com.singlelab.lume.util.TextInputDebounce
import com.singlelab.lume.util.removePostalCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_map.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment(), MapView, OnMapReadyCallback, OnPermissionListener {

    @Inject
    lateinit var daggerPresenter: MapPresenter

    @InjectPresenter
    lateinit var presenter: MapPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    private var mapFragment: SupportMapFragment? = null

    private var locationMap: GoogleMap? = null

    private var location = MapLocation()

    private val geoCoder: Geocoder by lazy { Geocoder(context, Const.RUS_LOCALE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        edit_text_search.apply {
            setSingleLine()
            setHint(getString(R.string.search_location))
            setStartDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search))
        }
        searchDebounce = TextInputDebounce(
            editText = edit_text_search.getEditText(),
            minimumSymbols = 4,
            delayMillis = 500
        )
        searchDebounce!!.watch {
            if (edit_text_search != null && edit_text_search.getFocused()) {
                location.latLong = null
                presenter.searchPlace(it)
            }
        }
        arguments?.let {
            location.address = MapFragmentArgs.fromBundle(it).locationName
            val xCoordinate = MapFragmentArgs.fromBundle(it).xCoordinate
            val yCoordinate = MapFragmentArgs.fromBundle(it).yCoordinate
            if (xCoordinate > 0 && yCoordinate > 0) {
                location.latLong = LatLng(xCoordinate.toDouble(), yCoordinate.toDouble())
            }
        }
        button_accept_location.setOnClickListener {
            if (edit_text_search.getText().isBlank()) {
                showSnackbar(getString(R.string.empty_location))
            } else {
                acceptLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        locationMap = googleMap
        (activity as MainActivity).checkLocationPermission()
    }

    override fun onLocationPermissionGranted() {
        showMap(true)
    }

    override fun onLocationPermissionDenied() {
        showMap(false)
    }

    override fun onContactsPermissionGranted() {
    }

    override fun onContactsPermissionDenied() {
    }

    override fun onWriteExternalPermissionGranted() {
    }

    override fun onWriteExternalPermissionDenied() {
    }

    override fun searchPlace(queryStr: String) {
        locationMap?.let { map ->
            try {
                val addresses: List<Address> =
                    geoCoder.getFromLocationName(queryStr, 1)
                if (addresses.isNotEmpty()) {
                    val lat = addresses[0].latitude
                    val lon = addresses[0].longitude
                    location.latLong = LatLng(lat, lon)
                    location.latLong?.let {
                        location.address =
                            addresses[0].getAddressLine(0).removePostalCode(addresses[0].postalCode)
                        location.city = addresses[0].locality
                        map.clear()
                        map.addMarker(MarkerOptions().position(it))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                showError(getString(R.string.error_address))
            }
        }
    }

    override fun searchPlace(latLng: LatLng) {
        try {
            val addresses: List<Address> =
                geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.isNotEmpty()) {
                location.latLong = latLng
                setLocationName(addresses[0])
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showError(getString(R.string.error_address))
        }
    }

    @SuppressLint("MissingPermission")
    private fun showMap(withMyLocation: Boolean) {
        locationMap?.let { map ->
            map.uiSettings.isMyLocationButtonEnabled = true
            map.isMyLocationEnabled = withMyLocation
            map.setOnMapClickListener {
                map.clear()
                map.addMarker(MarkerOptions().position(it))
                presenter.searchPlace(it)
            }
            if (location.latLong != null) {
                presenter.searchPlace(location.latLong!!)
                map.clear()
                map.addMarker(MarkerOptions().position(location.latLong!!))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location.latLong!!, 17f))
            } else {
                location.address?.let {
                    edit_text_search.setText(it)
                    presenter.searchPlace(it)
                }
            }
        }
    }

    private fun acceptLocation() {
        location.address?.let {
            parentFragmentManager.setFragmentResult(
                REQUEST_LOCATION,
                bundleOf(RESULT_LOCATION to location)
            )
            parentFragmentManager.popBackStack()
        }
    }

    private fun initMap() {
        mapFragment = SupportMapFragment()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.map_container, mapFragment!!)
            .commitNow()
        mapFragment?.getMapAsync(this)
    }

    private fun setLocationName(address: Address) {
        location.address = address.getAddressLine(0).removePostalCode(address.postalCode)
        location.city = address.locality
        edit_text_search.setText(location.address)
    }

    companion object {
        const val REQUEST_LOCATION = "REQUEST_LOCATION"
        const val RESULT_LOCATION = "RESULT_LOCATION"
    }
}