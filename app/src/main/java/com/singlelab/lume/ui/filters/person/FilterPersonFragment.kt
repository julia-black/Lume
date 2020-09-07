package com.singlelab.lume.ui.filters.person

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.ui.cities.CitiesFragment
import dagger.hilt.android.AndroidEntryPoint
import io.apptik.widget.MultiSlider
import kotlinx.android.synthetic.main.fragment_filters.text_city
import kotlinx.android.synthetic.main.fragment_filters_person.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class FilterPersonFragment : BaseFragment(), FilterPersonView {

    companion object {
        const val REQUEST_FILTER = "REQUEST_FILTER"
        const val RESULT_FILTER = "RESULT_FILTER"
    }

    @Inject
    lateinit var daggerPresenter: FilterPersonPresenter

    @InjectPresenter
    lateinit var presenter: FilterPersonPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.filterPerson = FilterPersonFragmentArgs.fromBundle(
                it
            ).filterPerson
        }
        showFilters()
        setListeners()
    }

    override fun showCity(cityName: String?) {
        text_city.text = cityName ?: getString(R.string.any_city)
    }

    private fun showFilters() {
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


    private fun setListeners() {
        text_city.setOnClickListener {
            toChooseCity()
        }

        seek_bar_age.setOnThumbValueChangeListener { _: MultiSlider?, _: MultiSlider.Thumb?, thumbIndex: Int, value: Int ->
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

    private fun toChooseCity() {
        val action =
            FilterPersonFragmentDirections.actionFiltersToCities()
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