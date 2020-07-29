package com.singlelab.lume.ui.swiper_person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnFilterListener
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.ui.swiper_person.adapter.CardStackPersonAdapter
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwiperPersonFragment : BaseFragment(), SwiperPersonView, OnlyForAuthFragments,
    CardStackListener, OnFilterListener {

    @Inject
    lateinit var daggerPresenter: SwiperPersonPresenter

    @InjectPresenter
    lateinit var presenter: SwiperPersonPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    private var cardStackManager: CardStackLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_swiper_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = ""
        arguments?.let {
            presenter.eventUid = SwiperPersonFragmentArgs.fromBundle(it).eventUid
        }
        setListeners()
        initCardStack()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showFilterInToolbar(true)
    }

    override fun onStop() {
        (activity as MainActivity).showFilterInToolbar(false)
        super.onStop()
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                presenter.invitePerson()
            }
            Direction.Left -> {
                presenter.rejectPerson()
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
        card_stack_view.adapter = CardStackPersonAdapter()
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setListeners() {
    }

    override fun showPerson(person: Person) {
        if (presenter.person == null) {
            presenter.loadRandomPerson()
        } else {
            (card_stack_view.adapter as CardStackPersonAdapter).setData(listOf(person))
        }
    }

    override fun toAcceptedPerson(person: Person, eventUid: String) {
        Toast.makeText(
            context,
            getString(R.string.person_invited, person.name),
            Toast.LENGTH_LONG
        ).show()
        presenter.loadRandomPerson()
    }

    override fun onClickFilter() {
        val action = SwiperPersonFragmentDirections.actionSwiperPersonToFilterFragment()
        action.isEvent = false
        findNavController().navigate(action)
    }
}