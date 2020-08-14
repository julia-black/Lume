package com.singlelab.lume.ui.event

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.custom.sliderimage.logic.SliderImage
import com.google.android.material.shape.CornerFamily
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.view.image_person.ImagePersonAdapter
import com.singlelab.lume.ui.view.image_person.OnPersonImageClickListener
import com.singlelab.lume.util.*
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.ParticipantStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.io.IOException
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EventFragment : BaseFragment(), EventView, OnlyForAuthFragments, OnPersonImageClickListener {

    companion object {
        const val REQUEST_EVENT = "REQUEST_EVENT"
        const val RESULT_EVENT = "RESULT_EVENT"
    }

    @Inject
    lateinit var daggerPresenter: EventPresenter

    @InjectPresenter
    lateinit var presenter: EventPresenter

    private val geoCoder: Geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.loadEvent(EventFragmentArgs.fromBundle(it).eventUid)
        }
        setListeners()
    }

    override fun showEvent(event: Event) {
        title.text = event.name
        val startDate = event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)
        val endDate = event.endTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)
        start_date.text = getString(R.string.date_start_title, startDate)
        end_date.text = getString(R.string.date_end_title, endDate)
        description.text = event.description
        if (event.isOnline) {
            text_location.visibility = View.INVISIBLE
            text_online.visibility = View.VISIBLE
        } else {
            text_online.visibility = View.INVISIBLE
            text_location.visibility = View.VISIBLE
            text_location.text = getLocationName(event.xCoordinate, event.yCoordinate) ?: getString(
                R.string.unavailable_location
            )
            text_location.setOnClickListener {
                val uri = String.format(
                    Locale.ENGLISH,
                    "geo:%f,%f?z=%d&q=%f,%f (%s)",
                    event.xCoordinate,
                    event.yCoordinate,
                    13,
                    event.xCoordinate,
                    event.yCoordinate,
                    event.name
                )
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context?.startActivity(intent)
            }
        }

        if (event.cityName == null || event.isOnline) {
            city.visibility = View.GONE
        } else {
            city.text = "${getString(R.string.city)}: ${event.cityName}"
            city.visibility = View.VISIBLE
        }

        val radius = resources.getDimension(R.dimen.radius_large)
        image.shapeAppearanceModel = image.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.CUT, 0f)
            .setBottomLeftCorner(CornerFamily.CUT, 0f)
            .build()

        if (event.eventPrimaryImageContentUid == null) {
            image.setImageResource(R.mipmap.image_event_default)
        } else {
            Glide.with(this)
                .load(event.eventPrimaryImageContentUid.generateImageLink())
                .into(image)
        }

        if (event.types.size == 2) {
            emoji_card_two.setMargin(right = resources.getDimension(R.dimen.margin_medium))
        } else if (event.types.size == 1) {
            emoji_card_one.setMargin(right = resources.getDimension(R.dimen.margin_medium))
        }
        event.types.forEachIndexed { index, eventType ->
            when (index) {
                0 -> {
                    emoji_card_one.visibility = View.VISIBLE
                    emoji_one.setImageResource(eventType.resId)
                }
                1 -> {
                    emoji_card_two.visibility = View.VISIBLE
                    emoji_two.setImageResource(eventType.resId)
                }
                2 -> {
                    emoji_card_three.visibility = View.VISIBLE
                    emoji_three.setImageResource(eventType.resId)
                }
            }
        }

        if (event.minAge == null && event.maxAge == null) {
            age.visibility = View.GONE
        } else if (event.maxAge == null) {
            age.text = getString(R.string.age_from, event.minAge)
        } else if (event.minAge == null) {
            age.text = getString(R.string.age_to, event.maxAge)
        } else {
            age.text = getString(
                R.string.age_from_to,
                event.minAge,
                event.maxAge
            )
        }
        count_participants.text = getString(
            R.string.count_participants,
            event.participants.size
        )
        count_participants.setOnClickListener {
            toParticipants(false, presenter.isAdministrator())
        }
        event.administrator?.let {
            administrator.text = getString(R.string.administrator, it.name)

            if (it.imageContentUid == null) {
                image_administrator.setImageResource(R.drawable.ic_profile)
            } else {
                Glide.with(this)
                    .load(it.imageContentUid.generateImageLink())
                    .into(image_administrator)
            }
        }

        if (event.participants.isEmpty()) {
            recycler_participants.visibility = View.GONE
        } else {
            recycler_participants.visibility = View.VISIBLE
            recycler_participants.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                visibility = View.VISIBLE
                adapter =
                    ImagePersonAdapter(
                        event.participants,
                        this@EventFragment
                    )
            }
        }
        if (event.notApprovedParticipants.isEmpty() || event.administrator?.personUid != AuthData.uid) {
            count_not_approved.visibility = View.GONE
        } else {
            count_not_approved.visibility = View.VISIBLE
            count_not_approved.text =
                getString(R.string.waiting_for_approve_users, event.notApprovedParticipants.size)
            count_not_approved.setOnClickListener {
                toParticipants(true, presenter.isAdministrator())
            }
        }

        if (event.invitedParticipants.isEmpty()) {
            count_invited.visibility = View.GONE
        } else {
            count_invited.visibility = View.VISIBLE
            count_invited.text = getString(R.string.count_invited, event.invitedParticipants.size)
        }

        if (presenter.isAdministrator()) {
            button_invite.visibility = View.VISIBLE
            button_search_participants.visibility = View.VISIBLE
            button_search_participants.setOnClickListener {
                if (event.cityId != null && event.cityName != null && !event.isOnline) {
                    toSwiperPeople(event.eventUid, City(event.cityId, event.cityName))
                } else {
                    toSwiperPeople(event.eventUid)
                }
            }
        } else {
            button_search_participants.visibility = View.GONE
            button_invite.visibility = if (event.isOpenForInvitations) View.VISIBLE else View.GONE
        }

        if (event.eventPrimaryImageContentUid != null) {
            image.setOnClickListener {
                showFullScreenImages(event.eventPrimaryImageContentUid, event.images)
            }
        }

        if (event.isOpenForInvitations) {
            button_join.text = getString(R.string.join)
        } else {
            button_join.text = getString(R.string.add_request)
        }

        val currentUid = AuthData.uid ?: return

        when (event.getParticipantStatus(currentUid)) {
            ParticipantStatus.ACTIVE -> {
                button_join.visibility = View.GONE
                button_chat.visibility = View.VISIBLE
                button_accept.visibility = View.GONE
                button_reject.visibility = View.GONE
            }
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT -> {
                button_join.visibility = View.VISIBLE
                button_join.isEnabled = false
                button_chat.visibility = View.GONE
                button_accept.visibility = View.GONE
                button_reject.visibility = View.GONE
            }
            ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER -> {
                button_chat.visibility = View.GONE
                button_join.visibility = View.GONE
                button_invite.visibility = View.GONE
                button_accept.visibility = View.VISIBLE
                button_reject.visibility = View.VISIBLE
                button_accept.setOnClickListener {
                    parentFragmentManager.setFragmentResult(
                        REQUEST_EVENT, bundleOf(RESULT_EVENT to event.eventUid)
                    )
                    presenter.acceptEvent(currentUid, event.eventUid)
                }
                button_reject.setOnClickListener {
                    presenter.rejectEvent(currentUid, event.eventUid)
                }
            }
            null -> {
                button_accept.visibility = View.GONE
                button_reject.visibility = View.GONE
                button_chat.visibility = View.GONE
                button_invite.visibility = View.GONE
                button_join.visibility = View.VISIBLE
            }
        }
    }

    private fun showFullScreenImages(primaryImageContentUid: String, images: List<String>?) {
        context?.let {
            val allImages = mutableListOf(primaryImageContentUid)
            if (!images.isNullOrEmpty()) {
                allImages.addAll(images)
            }
            val links = allImages.map { image ->
                image.generateImageLink()
            }
            SliderImage.openfullScreen(it, links, 0)
        }
    }

    private fun getLocationName(xCoordinate: Double?, yCoordinate: Double?): String? {
        if (xCoordinate == null || yCoordinate == null) {
            return null
        }
        return try {
            val addresses: List<Address> =
                geoCoder.getFromLocation(xCoordinate, yCoordinate, 1)
            if (addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0).removePostalCode(addresses[0].postalCode)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }

    private fun toSwiperPeople(eventUid: String?, city: City? = null) {
        eventUid?.let {
            val action = EventFragmentDirections.actionEventToSwiperPerson(eventUid)
            action.city = city
            findNavController().navigate(action)
        }
    }

    private fun toParticipants(withNotApproved: Boolean, isAdministrator: Boolean = false) {
        presenter.getEventUid() ?: return
        presenter.getParticipant(withNotApproved)?.let {
            val action =
                EventFragmentDirections.actionEventToParticipants(presenter.getEventUid()!!, it)
            action.withNotApproved = withNotApproved
            action.isAdministrator = isAdministrator
            findNavController().navigate(action)
        }
    }

    override fun toMyProfile() {
        findNavController().navigate(R.id.action_event_to_my_profile)
    }

    override fun toProfile(personUid: String) {
        findNavController().navigate(EventFragmentDirections.actionEventToPerson(personUid))
    }

    override fun onRejectedEvent() {
        parentFragmentManager.setFragmentResult(
            REQUEST_EVENT, bundleOf(RESULT_EVENT to presenter.event?.eventUid)
        )
        parentFragmentManager.popBackStack()
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(EventFragmentDirections.actionEventToPerson(personUid))
    }

    private fun setListeners() {
        image_administrator.setOnClickListener {
            presenter.onClickAdministrator()
        }
        administrator.setOnClickListener {
            presenter.onClickAdministrator()
        }
        button_chat.setOnClickListener {
            val event = presenter.event
            if (event?.chatUid != null) {
                toChat(event.name, event.chatUid)
            }
        }
        button_invite.setOnClickListener {
            presenter.event?.eventUid?.let { eventUid ->
                toInvite(eventUid)
            }
        }
        button_join.setOnClickListener {
            presenter.joinEvent()
        }
        button_share.setOnClickListener {
            presenter.event?.let {
                shareEvent(it.eventUid, it.name)
            }
        }
    }

    private fun shareEvent(eventUid: String?, name: String) {
        eventUid?.let {
            shareText(eventUid.generateEventLink())
        }
    }

    private fun toChat(eventName: String, chatUid: String) {
        findNavController().navigate(
            EventFragmentDirections.actionFromEventToChat(
                ChatOpeningInvocationType.Common(
                    title = eventName,
                    chatUid = chatUid,
                    isGroup = true
                )
            )
        )
    }

    private fun toInvite(eventUid: String) {
        val action = EventFragmentDirections.actionEventToFriends()
        action.eventUid = eventUid
        findNavController().navigate(action)
    }
}