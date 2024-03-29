package com.singlelab.lume.ui.swiper_event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.filters.event.FilterFragment
import com.singlelab.lume.ui.swiper_event.adapter.CardStackEventAdapter
import com.singlelab.lume.ui.swiper_event.adapter.OnCardEventListener
import com.singlelab.lume.ui.view.dialog.OnDialogViewClickListener
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_swiper_event.*
import kotlinx.android.synthetic.main.view_template_card.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SwiperEventFragment : BaseFragment(), SwiperEventView, OnlyForAuthFragments,
    CardStackListener, OnCardEventListener, OnDialogViewClickListener {

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
            view_template_card.visibility = View.VISIBLE
            icon_empty_events.visibility = View.GONE
            card_empty_events.visibility = View.GONE
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
        view_template_card.visibility = View.GONE
        card_empty_events.visibility = View.VISIBLE
        if (isFullFilter) {
            button_filter.visibility = View.GONE
            icon_empty_events.visibility = View.VISIBLE
            icon_empty_events_full_filter.visibility = View.VISIBLE
            text_empty_swipes.text = getString(R.string.empty_swipes)
        } else {
            icon_empty_events_full_filter.visibility = View.GONE
            icon_empty_events.visibility = View.GONE
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

    override fun showInfoDialog() {
        info_dialog.setDialogListener(this)
        info_dialog.isVisible = true
    }

    override fun showSuccessReport() {
        showSnackbar(getString(R.string.report_send), ToastType.SUCCESS)
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    override fun updateNewYearPromo(newYearPromoRewardEnabled: Boolean) {
        super.updateNewYearPromo(newYearPromoRewardEnabled)
        showNewYearImage(newYearPromoRewardEnabled)
    }

    override fun showNewYearImage(isNewYear: Boolean) {
        context?.let {
            icon_empty_events.setImageDrawable(
                ContextCompat.getDrawable(
                    it,
                    if (isNewYear) R.drawable.ic_not_events_new_year else R.drawable.ic_not_events
                )
            )
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

    override fun onAdministratorClick(personUid: String) {
        findNavController().navigate(
            SwiperEventFragmentDirections.actionSwiperEventToPerson(
                personUid
            )
        )
    }

    override fun onImageClick(images: List<String>) {
        val action =
            SwiperEventFragmentDirections.actionSwiperEventToImageSlider(images.toTypedArray())
        findNavController().navigate(action)
    }

    override fun onReportClick() {
        setSoftInputType(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        showEditTextDialog(
            title = getString(R.string.enter_reason_report),
            emptyText = getString(R.string.empty_reason),
            callback = {
                hideKeyboard()
                presenter.sendReport(it)
            },
            cancelCallback = {
                hideKeyboard()
            }
        )
    }

    override fun onCloseDialogClick() {
        info_dialog.isVisible = false
    }

    override fun onLinkClick(url: String) {
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
        button_filter.setOnClickListener {
            button_filter.isEnabled = false
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
}