package com.singlelab.lume.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.singlelab.data.model.event.EventSummary
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.events.adapter.EventsAdapter
import com.singlelab.lume.ui.events.adapter.OnEventItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_events.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : BaseFragment(), EventsView, OnlyForAuthFragments, OnEventItemClickListener {

    @Inject
    lateinit var daggerPresenter: EventsPresenter

    @InjectPresenter
    lateinit var presenter: EventsPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_events)
        recycler_events.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        button_create_event.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.action_events_to_creating_event)
                .onClick(view)
        }
    }

    override fun showEvents(events: List<EventSummary>) {
        recycler_events.apply {
            adapter = EventsAdapter(events, this@EventsFragment)
        }
    }

    override fun onClickEvent(uid: String) {
        val action = EventsFragmentDirections.actionEventsToEvent(uid)
        findNavController().navigate(action)
    }
}