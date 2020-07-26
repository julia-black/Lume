package com.singlelab.lume.ui.swiper_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnSearchListener
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.ui.swiper_event.adapter.CardStackAdapter
import com.singlelab.lume.util.generateImageLink
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwiperEventFragment : BaseFragment(), SwiperEventView, OnlyForAuthFragments,
    CardStackListener, OnSearchListener {

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
    }

    override fun onStop() {
        (activity as MainActivity?)?.showSearchInToolbar(false)
        super.onStop()
    }

    override fun showEvent(event: Event) {
        if (presenter.event == null) {
            presenter.loadRandomEvent()
        } else {
            (card_stack_view.adapter as CardStackAdapter).setEvents(listOf(event))
        }
    }

    override fun toAcceptedEvent(eventUid: String) {
        findNavController().navigate(SwiperEventFragmentDirections.actionSwiperEventToEvent(eventUid))
    }

    override fun onClickSearch() {
        findNavController().navigate(SwiperEventFragmentDirections.actionSwiperEventToSearchEvent())
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                //todo если пользователь не зареган, то отправлять на форму регистрации (запоминая событие, на которое он хотел пойти)
                presenter.acceptEvent()
            }
            Direction.Left -> {
                presenter.loadRandomEvent()
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
        card_stack_view.adapter = CardStackAdapter()
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setListeners() {
    }

    private fun showImage(imageView: ImageView, imageUid: String) {
        Glide.with(this)
            .load(imageUid.generateImageLink())
            .into(imageView)
    }
}