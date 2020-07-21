package com.singlelab.lume.ui.swiper_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.singlelab.data.model.event.Event
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.ui.swiper_event.adapter.CardStackAdapter
import com.singlelab.lume.util.generateImageLink
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import kotlinx.coroutines.delay
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwiperEventFragment : BaseFragment(), SwiperEventView, OnlyForAuthFragments,
    CardStackListener {

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
        presenter.loadNextEvent()
    }

    override fun showEvent(event: Event) {
        (card_stack_view.adapter as CardStackAdapter).setEvents(listOf(event))
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            //todo если вправо, то переход к чату события? или в мои события? или продолжаем свайпать?
            Direction.Right, Direction.Left -> {
                super.showLoading(true)
                invokeSuspend {
                    delay(2000).run {
                        presenter.loadNextEvent()
                    }
                }
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