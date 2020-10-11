package com.singlelab.lume.ui.swiper_person

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
import com.singlelab.lume.model.profile.FilterPerson
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.chat.common.view.OnClickImageListener
import com.singlelab.lume.ui.filters.person.FilterPersonFragment
import com.singlelab.lume.ui.swiper_person.adapter.CardStackPersonAdapter
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_person.*
import kotlinx.android.synthetic.main.view_template_person.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwiperPersonFragment : BaseFragment(), SwiperPersonView, OnlyForAuthFragments,
    CardStackListener {

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
        arguments?.let {
            presenter.eventUid = SwiperPersonFragmentArgs.fromBundle(it).eventUid
            val city = SwiperPersonFragmentArgs.fromBundle(it).city
            if (presenter.filterPerson == null) {
                presenter.filterPerson = FilterPerson(city?.cityId, city?.cityName)
            }
        }
        setListeners()
        initCardStack()
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
        val listenerImage = object : OnClickImageListener {
            override fun onClickImage(imageUids: List<String>) {
                navigateToImages(imageUids)
            }
        }
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
        card_stack_view.adapter = CardStackPersonAdapter(listener = listenerImage)
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun navigateToImages(imageUids: List<String>) {
        findNavController().navigate(
            SwiperPersonFragmentDirections.actionSwiperPersonToImageSlider(
                imageUids.toTypedArray()
            )
        )
    }

    private fun setListeners() {
        button_filter.setOnClickListener {
            val action = SwiperPersonFragmentDirections.actionSwiperPersonToFiltersPerson()
            presenter.filterPerson?.let {
                val filterPerson = it.copy()
                action.filterPerson = filterPerson
                findNavController().navigate(action)
            }
        }
        parentFragmentManager.setFragmentResultListener(
            FilterPersonFragment.REQUEST_FILTER,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == FilterPersonFragment.REQUEST_FILTER) {
            val filterPerson: FilterPerson =
                result.getParcelable(FilterPersonFragment.RESULT_FILTER) ?: return
            presenter.applyFilter(filterPerson)
        }
    }

    override fun showPerson(person: Person) {
        if (presenter.person == null) {
            presenter.loadRandomPerson()
        } else {
            button_filter.visibility = View.VISIBLE
            view_template_person.visibility = View.VISIBLE
            card_stack_view.visibility = View.VISIBLE
            text_empty_swipes.visibility = View.GONE
            (card_stack_view.adapter as CardStackPersonAdapter).setData(listOf(person))
        }
    }

    override fun toAcceptedPerson(person: Person, eventUid: String) {
        showSnackbar(getString(R.string.person_invited, person.name), ToastType.SUCCESS)
        presenter.loadRandomPerson()
    }

    override fun showEmptySwipes() {
        view_template_person.visibility = View.GONE
        card_stack_view.visibility = View.GONE
        text_empty_swipes.visibility = View.VISIBLE
        text_empty_swipes.text = getString(R.string.empty_persons)
        button_filter.visibility = View.VISIBLE
    }
}