package com.singlelab.lume.ui.swiper_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnFilterListener
import com.singlelab.lume.base.listeners.OnSearchListener
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.ui.filters.FilterFragment
import com.singlelab.lume.ui.swiper_event.adapter.CardStackEventAdapter
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwiperEventFragment : BaseFragment(), SwiperEventView, OnlyForAuthFragments,
    CardStackListener, OnSearchListener, OnFilterListener {

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
        activity?.title = ""
        setListeners()
        initCardStack()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)?.showSearchInToolbar(true)
        (activity as MainActivity?)?.showFilterInToolbar(true)
    }

    override fun onStop() {
        (activity as MainActivity?)?.showSearchInToolbar(false)
        (activity as MainActivity?)?.showFilterInToolbar(false)
        super.onStop()
    }

    override fun showEvent(event: Event) {
        if (presenter.event == null) {
            presenter.loadRandomEvent()
        } else {
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

    override fun showEmptySwipes() {
        card_stack_view.visibility = View.GONE
        text_empty_swipes.visibility = View.VISIBLE
    }

    override fun onClickSearch() {
        findNavController().navigate(SwiperEventFragmentDirections.actionSwiperEventToSearchEvent())
    }

    override fun onClickFilter() {
        val action = SwiperEventFragmentDirections.actionSwiperEventToFilterFragment()
        action.isEvent = true
        action.filterEvent = presenter.filterEvent
        findNavController().navigate(action)
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
        card_stack_view.adapter = CardStackEventAdapter()
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setListeners() {
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
}