package com.singlelab.lume.ui.swiper_event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.ui.filters.event.FilterFragment
import com.singlelab.lume.ui.swiper_event.adapter.CardStackEventAdapter
import com.singlelab.lume.ui.swiper_event.adapter.OnCardEventListener
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SwiperEventFragment : BaseFragment(), SwiperEventView, OnlyForAuthFragments,
    CardStackListener, OnCardEventListener {

    @Inject
    lateinit var daggerPresenter: SwiperEventPresenter

    @InjectPresenter
    lateinit var presenter: SwiperEventPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var cardStackManager: CardStackLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_swiper_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initCardStack()
    }

    override fun showEvent(event: Event) {
        if (presenter.event == null) {
            presenter.loadRandomEvent()
        } else {
            button_filter.visibility = View.VISIBLE
            card_stack_view.visibility = View.VISIBLE
            text_empty_swipes.visibility = View.GONE
            (card_stack_view.adapter as CardStackEventAdapter).setEvents(listOf(event))
        }
    }

    override fun toAcceptedEvent(isOpenEvent: Boolean, eventUid: String) {
        if (isOpenEvent) {
            findNavController().navigate(
                SwiperEventFragmentDirections.actionSwiperEventToEvent(
                    eventUid
                )
            )
        } else {
            presenter.loadRandomEvent()
        }
    }

    override fun showEmptySwipes(isFullFilter: Boolean) {
        card_stack_view.visibility = View.GONE
        text_empty_swipes.visibility = View.VISIBLE
        if (isFullFilter) {
            button_filter.visibility = View.GONE
            text_empty_swipes.text = getString(R.string.empty_swipes)
        } else {
            button_filter.visibility = View.VISIBLE
            text_empty_swipes.text = getString(R.string.empty_swipes_expand_filters)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                presenter.acceptEvent()
            }
            Direction.Left -> {
                presenter.rejectEvent()
            }
            else -> {
            }
        }
    }

    override fun hideFilter() {
        button_filter.visibility = View.GONE
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private fun initCardStack() {
        cardStackManager = CardStackLayoutManager(context, this)
        cardStackManager?.apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        card_stack_view.layoutManager = cardStackManager
        card_stack_view.adapter = CardStackEventAdapter(listener = this)
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setListeners() {
//        button_search.setOnClickListener {
//            findNavController().navigate(SwiperEventFragmentDirections.actionSwiperEventToSearchEvent())
//        }
        button_filter.setOnClickListener {
            val action = SwiperEventFragmentDirections.actionSwiperEventToFilterFragment()
            action.isEvent = true
            presenter.filterEvent.let {
                val filterEvent = it.copy(selectedTypes = it.selectedTypes.toMutableList())
                action.filterEvent = filterEvent
                findNavController().navigate(action)
            }
        }
        parentFragmentManager.setFragmentResultListener(
            FilterFragment.REQUEST_FILTER,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == FilterFragment.REQUEST_FILTER) {
            val filterEvent: FilterEvent =
                result.getParcelable(FilterFragment.RESULT_FILTER) ?: return
            presenter.applyFilter(filterEvent)
        }
    }

    override fun onLocationClick(lat: Double, long: Double, name: String) {
        val uri = String.format(
            Locale.ENGLISH,
            "geo:%f,%f?z=%d&q=%f,%f (%s)",
            lat,
            long,
            13,
            lat,
            long,
            name
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context?.startActivity(intent)
    }
}