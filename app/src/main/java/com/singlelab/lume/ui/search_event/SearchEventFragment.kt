package com.singlelab.lume.ui.search_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.event.EventSummary
import com.singlelab.lume.ui.search_event.adapter.EventsAdapter
import com.singlelab.lume.ui.view.event.OnEventItemClickListener
import com.singlelab.lume.util.TextInputDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SearchEventFragment : BaseFragment(), SearchEventView,
    OnEventItemClickListener {

    @Inject
    lateinit var daggerPresenter: SearchEventPresenter

    @InjectPresenter
    lateinit var presenter: SearchEventPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var searchDebounce: TextInputDebounce? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_search_results.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            visibility = View.VISIBLE
            adapter = EventsAdapter(
                mutableListOf(),
                this@SearchEventFragment
            )
        }

        searchDebounce = TextInputDebounce(
            editText = edit_text_search,
            isHandleEmptyString = true
        )
        searchDebounce!!.watch {
            presenter.search(it)
        }
    }

    override fun showSearchResults(events: List<EventSummary>?) {
        if (events.isNullOrEmpty()) {
            title_empty_search.visibility = View.VISIBLE
            recycler_search_results.visibility = View.GONE
        } else {
            (recycler_search_results.adapter as EventsAdapter).setData(events)
            title_empty_search.visibility = View.GONE
            recycler_search_results.visibility = View.VISIBLE
        }
    }

    override fun showEmptyQuery() {
        title_empty_search.visibility = View.GONE
        recycler_search_results.visibility = View.GONE
    }

    override fun onClickEvent(uid: String) {
        //todo подумать, куда именно будет переход
    }
}