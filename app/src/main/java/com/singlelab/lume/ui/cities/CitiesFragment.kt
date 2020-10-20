package com.singlelab.lume.ui.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.city.City
import com.singlelab.lume.ui.cities.adapter.CitiesAdapter
import com.singlelab.lume.ui.cities.adapter.OnCityClickListener
import com.singlelab.lume.util.TextInputDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cities.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : BaseFragment(), CitiesView, OnCityClickListener {

    companion object {
        const val REQUEST_CITY = "REQUEST_CITY"
        const val RESULT_CITY = "RESULT_CITY"
    }

    @Inject
    lateinit var daggerPresenter: CitiesPresenter

    @InjectPresenter
    lateinit var presenter: CitiesPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        arguments?.let {
            presenter.containsAnyCity = CitiesFragmentArgs.fromBundle(it).containAnyCity
        }

        recycler_cities.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            visibility = View.VISIBLE
            adapter = CitiesAdapter(this@CitiesFragment)
        }

        choose_any_city.setOnClickListener {
            parentFragmentManager.setFragmentResult(REQUEST_CITY, bundleOf(RESULT_CITY to null))
            parentFragmentManager.popBackStack()
        }

        edit_text_search.apply {
            setSingleLine()
            setHint(getString(R.string.search_city))
            setStartDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search))
        }
        searchDebounce = TextInputDebounce(
            editText = edit_text_search.getEditText(),
            isHandleEmptyString = true
        )
        searchDebounce!!.watch {
            presenter.filter(it)
        }
    }

    override fun showCities(cities: List<City>?, containsAnyCity: Boolean) {
        if (cities.isNullOrEmpty()) {
            showEmptySearch()
        } else {
            if (containsAnyCity) {
                choose_any_city.visibility = View.VISIBLE
            } else {
                choose_any_city.visibility = View.GONE
            }
            title_empty_search.visibility = View.GONE
            recycler_cities.visibility = View.VISIBLE
            (recycler_cities.adapter as CitiesAdapter).setData(cities)
        }
    }

    override fun onCityClick(city: City) {
        parentFragmentManager.setFragmentResult(REQUEST_CITY, bundleOf(RESULT_CITY to city))
        parentFragmentManager.popBackStack()
    }

    private fun showEmptySearch() {
        title_empty_search.visibility = View.VISIBLE
        recycler_cities.visibility = View.GONE
    }
}