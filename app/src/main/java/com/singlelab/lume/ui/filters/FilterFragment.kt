package com.singlelab.lume.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Distance
import com.singlelab.lume.ui.cities.CitiesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filters.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment : BaseFragment(), FilterView {

    @Inject
    lateinit var daggerPresenter: FilterPresenter

    @InjectPresenter
    lateinit var presenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

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
            presenter.isEvent = FilterFragmentArgs.fromBundle(it).isEvent
        }
        showFilters(presenter.isEvent)
        setListeners()
    }

    override fun showDistance(distance: Distance) {
        text_distance.text = distance.title
    }

    override fun showCity(cityName: String) {
        text_city.text = cityName
    }

    private fun showFilters(isEvent: Boolean) {
        if (isEvent) {
            chip_groups.visibility = View.VISIBLE
            seek_bar_distance.visibility = View.VISIBLE
            presenter.filterEvent.let {
                seek_bar_distance.progress = it.distance.id
                text_distance.text = it.distance.title
                text_city.text = it.cityName
            }
        } else {
            chip_groups.visibility = View.GONE
            seek_bar_distance.visibility = View.GONE
            text_distance.visibility = View.GONE
        }
    }

    private fun setListeners() {
        seek_bar_distance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.changeDistance(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        text_city.setOnClickListener {
            toChooseCity()
        }
        parentFragmentManager.setFragmentResultListener(
            CitiesFragment.REQUEST_CITY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
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
}