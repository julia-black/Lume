package com.singlelab.lume.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.util.TextInputDebounce
import com.singlelab.lume.util.removePostalCode
import com.singlelab.net.model.auth.AuthData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_map.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.io.IOException
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment(), MapView, OnMapReadyCallback {

    companion object {
        const val REQUEST_LOCATION = "REQUEST_LOCATION"
        const val RESULT_LOCATION_NAME = "RESULT_LOCATION_NAME"
        const val RESULT_LOCATION_COORDINATE = "RESULT_LOCATION_COORDINATE"
    }

    @Inject
    lateinit var daggerPresenter: MapPresenter

    @InjectPresenter
    lateinit var presenter: MapPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    private var mapFragment: SupportMapFragment? = null

    private var locationMap: GoogleMap? = null

    private var locationName: String? = null

    private var locationCoordinate: LatLng? = null

    private val geoCoder: Geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_location_events)
        initMap()
        searchDebounce =
            TextInputDebounce(editText = edit_text_search, minimumSymbols = 4, delayMillis = 500)
        searchDebounce!!.watch {
            if (edit_text_search.isFocused) {
                presenter.searchPlace(it)
            }
        }
        arguments?.let {
            locationName = MapFragmentArgs.fromBundle(it).locationName
        }
        button_accept_location.setOnClickListener {
            if (edit_text_search.text.isBlank()) {
                Toast.makeText(context, getString(R.string.empty_location), Toast.LENGTH_LONG)
                    .show()
            } else {
                acceptLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        locationMap = googleMap
        locationMap?.let { map ->
            locationMap!!.uiSettings.isMyLocationButtonEnabled = true
            context?.let {
                if (!(ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)
                ) {
                    locationMap!!.isMyLocationEnabled = true
                }
            }
            map.setOnMapClickListener {
                map.clear()
                map.addMarker(MarkerOptions().position(it))
                presenter.searchPlace(it)
            }
            if (locationName != null) {
                edit_text_search.setText(locationName)
                presenter.searchPlace(locationName!!)
            } else {
                AuthData.cityName?.let {
                    presenter.searchPlace(it)
                }
            }
        }
    }

    override fun searchPlace(queryStr: String) {
        locationMap?.let { map ->
            try {
                val addresses: List<Address> =
                    geoCoder.getFromLocationName("${AuthData.cityName}, $queryStr", 1)
                if (addresses.isNotEmpty()) {
                    val lat = addresses[0].latitude
                    val lon = addresses[0].longitude
                    locationCoordinate = LatLng(lat, lon)
                    locationCoordinate?.let {
                        locationName =
                            addresses[0].getAddressLine(0).removePostalCode(addresses[0].postalCode)
                        map.clear()
                        map.addMarker(MarkerOptions().position(it))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, getString(R.string.error_address), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun searchPlace(latLng: LatLng) {
        try {
            val addresses: List<Address> =
                geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.isNotEmpty()) {
                locationCoordinate = latLng
                setLocationName(addresses[0])
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, getString(R.string.error_address), Toast.LENGTH_LONG).show()
        }
    }

    private fun acceptLocation() {
        locationName?.let {
            parentFragmentManager.setFragmentResult(
                REQUEST_LOCATION,
                bundleOf(
                    RESULT_LOCATION_NAME to locationName,
                    RESULT_LOCATION_COORDINATE to locationCoordinate
                )
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
        locationName = address.getAddressLine(0).removePostalCode(address.postalCode)
        edit_text_search.setText(locationName)
    }
}