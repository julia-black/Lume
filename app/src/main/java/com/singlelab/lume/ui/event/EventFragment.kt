package com.singlelab.lume.ui.event

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.location.MapLocation
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.ui.map.MapFragment
import com.singlelab.lume.ui.view.image_person.ImagePersonAdapter
import com.singlelab.lume.ui.view.image_person.OnPersonImageClickListener
import com.singlelab.lume.util.*
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.ParticipantStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.description
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EventFragment : BaseFragment(), EventView, OnlyForAuthFragments, OnPersonImageClickListener,
    OnActivityResultListener {

    companion object {
        const val REQUEST_EVENT = "REQUEST_EVENT"
        const val RESULT_EVENT = "RESULT_EVENT"
        const val MAX_VIEW_PARTICIPANTS = 3
    }

    @Inject
    lateinit var daggerPresenter: EventPresenter

    @InjectPresenter
    lateinit var presenter: EventPresenter

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
        showTime(event.startTime, event.endTime)
        description.text = event.description
        if (event.status.titleRes == null) {
            status.visibility = View.GONE
        } else {
            status.visibility = View.VISIBLE
            status.setText(event.status.titleRes)
        }
        if (event.isOnline) {
            text_location.text = context?.getString(R.string.online)
            icon_location.setImageResource(R.drawable.ic_online)
        } else {
            text_location.text =
                context?.getLocationName(event.xCoordinate, event.yCoordinate) ?: event.cityName
            if (event.xCoordinate != null && event.xCoordinate > 0
                && event.yCoordinate != null && event.yCoordinate > 0
            ) {
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
                    if (presenter.isAdministrator()) {
                        showLocationAction(
                            uri,
                            event.cityName ?: "",
                            event.xCoordinate,
                            event.yCoordinate
                        )
                    } else {
                        openBrowser(uri)
                    }
                }
            } else {
                text_location.setOnClickListener {
                    if (presenter.isAdministrator()) {
                        showLocationAction(
                            null,
                            event.cityName ?: "",
                            event.xCoordinate,
                            event.yCoordinate
                        )
                    }
                }
            }
        }

        if (event.eventPrimaryImageContentUid == null) {
            image.setImageResource(R.mipmap.image_event_default)
        } else {
            Glide.with(this)
                .load(event.eventPrimaryImageContentUid.generateImageLink())
                .thumbnail(0.3f)
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
            administrator_name.text = it.name

            if (it.imageContentUid != null) {
                Glide.with(this)
                    .load(it.imageContentUid.generateMiniImageLink())
                    .into(image_administrator)
            }
        }

        if (event.participants.isEmpty()) {
            recycler_participants.visibility = View.GONE
            more_participants.visibility = View.GONE
        } else {
            val participantSize = event.participants.size
            recycler_participants.visibility = View.VISIBLE
            recycler_participants.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                visibility = View.VISIBLE
                adapter =
                    ImagePersonAdapter(
                        event.participants.subList(
                            0,
                            if (participantSize < MAX_VIEW_PARTICIPANTS) participantSize else MAX_VIEW_PARTICIPANTS
                        ),
                        this@EventFragment
                    )
            }
            if (participantSize > MAX_VIEW_PARTICIPANTS) {
                more_participants.visibility = View.VISIBLE
                more_participants.text = "+${participantSize - MAX_VIEW_PARTICIPANTS}"
                more_participants.setOnClickListener {
                    toParticipants(false, presenter.isAdministrator())
                }
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

        if (presenter.isAdministrator() && event.isActive()) {
            button_invite.visibility = View.VISIBLE
            button_search_participants.visibility = View.VISIBLE
            button_search_participants.setOnClickListener {
                if (event.cityId != null && event.cityName != null && !event.isOnline) {
                    toSwiperPeople(event.eventUid, City(event.cityId, event.cityName))
                } else {
                    toSwiperPeople(event.eventUid)
                }
            }
            button_cancel_or_leave_event.visibility = View.VISIBLE
            icon_edit_description.visibility = View.VISIBLE
            icon_edit_description.setOnClickListener {
                editDescription(description.text.toString())
            }
        } else {
            icon_edit_description.visibility = View.GONE
            button_search_participants.visibility = View.GONE
            button_invite.visibility =
                if (event.isOpenForInvitations && event.isActive()) View.VISIBLE else View.GONE
            button_cancel_or_leave_event.visibility = View.GONE
            divider_three.visibility = View.GONE
        }

        image.setOnClickListener {
            if (presenter.isAdministrator()) {
                showImagesAction(event)
            } else {
                if (event.eventPrimaryImageContentUid != null) {
                    showFullScreenImages(event.eventPrimaryImageContentUid, event.images)
                }
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
                if (!presenter.isAdministrator()) {
                    button_cancel_or_leave_event.text = getString(R.string.leave_event)
                    button_cancel_or_leave_event.visibility = View.VISIBLE
                    divider_three.visibility = View.VISIBLE
                    if (!event.isOpenForInvitations) {
                        divider_four.visibility = View.GONE
                    }
                }
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
                divider_four.visibility = View.GONE
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

        if (!event.isActive()) {
            divider_three.visibility = View.GONE
            divider_four.visibility = View.GONE
            button_cancel_or_leave_event.visibility = View.GONE
            button_join.visibility = View.GONE
            button_accept.visibility = View.GONE
            button_reject.visibility = View.GONE
        }

        if (!presenter.isAdministrator()) {
            button_report_event.visibility = View.VISIBLE
            divider_three.visibility = View.VISIBLE
        }
        presenter.getAvailablePromoReward()

        button_receive_reward.setOnClickListener {
            event.eventUid?.let {
                findNavController().navigate(
                    EventFragmentDirections.actionFromEventToReceiveReward(
                        event.eventUid
                    )
                )
            }
        }

        button_report_event.setOnClickListener {
            showReport()
        }

        button_cancel_or_leave_event.setOnClickListener {
            if (presenter.isAdministrator()) {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                presenter.cancelEvent()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                showDialog(
                    getString(R.string.accept_cancel),
                    getString(R.string.info_about_cancel),
                    dialogClickListener
                )
            } else {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                presenter.rejectEvent(currentUid, event.eventUid)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                showDialog(
                    getString(R.string.accept_leave),
                    getString(R.string.info_about_leave),
                    dialogClickListener
                )
            }
        }
    }

    private fun showReport() {
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

    override fun toProfile(personUid: String) {
        if (personUid == AuthData.uid) {
            findNavController().navigate(R.id.action_event_to_my_profile)
        } else {
            findNavController().navigate(EventFragmentDirections.actionEventToPerson(personUid))
        }
    }

    override fun onRejectedEvent() {
        parentFragmentManager.setFragmentResult(
            REQUEST_EVENT, bundleOf(RESULT_EVENT to presenter.event?.eventUid)
        )
        parentFragmentManager.popBackStack()
    }

    override fun toInviteFriends(eventUid: String, allParticipantIds: List<String>?) {
        val action = EventFragmentDirections.actionEventToFriends()
        action.eventUid = eventUid
        action.participantIds = allParticipantIds?.toTypedArray()
        findNavController().navigate(action)
    }

    override fun onPersonClick(personUid: String) {
        toProfile(personUid)
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(
                requestCode,
                resultCode,
                data,
                Const.SELECT_IMAGE_REQUEST_CODE
            )
        ) {
            val images = ImagePicker.getImages(data)
                .mapNotNull { it.uri.getBitmap(activity?.contentResolver) }
            if (resultCode == Activity.RESULT_OK && images.isNotEmpty()) {
                presenter.updateEvent(images = images)
            } else {
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun showPromoReceiveButton(isShow: Boolean) {
        divider_four.isVisible = isShow
        button_receive_reward.isVisible = isShow
    }

    override fun showSuccessReport() {
        showSnackbar(getString(R.string.report_send), ToastType.SUCCESS)
    }

    private fun showImagesAction(event: Event) {
        if (event.eventPrimaryImageContentUid != null) {
            showListDialog(
                getString(R.string.choose_action),
                arrayOf(
                    getString(R.string.show_images),
                    getString(R.string.add_images)
                ), DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> showFullScreenImages(
                            event.eventPrimaryImageContentUid,
                            event.images
                        )
                        1 -> if (event.images == null || event.images.size < 10) {
                            onClickAddImages()
                        }
                    }
                }
            )
        } else {
            showListDialog(
                getString(R.string.choose_action),
                arrayOf(getString(R.string.add_images)),
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> onClickAddImages()
                    }
                }
            )
        }
    }

    private fun showLocationAction(
        uri: String? = null,
        cityName: String,
        xCoordinate: Double? = null,
        yCoordinate: Double? = null
    ) {
        if (uri != null) {
            showListDialog(
                getString(R.string.choose_action),
                arrayOf(
                    getString(R.string.open_map),
                    getString(R.string.change_location)
                ), DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> openBrowser(uri)
                        1 -> toChooseLocation(cityName, xCoordinate, yCoordinate)
                    }
                }
            )
        } else {
            showListDialog(
                getString(R.string.choose_action),
                arrayOf(
                    getString(R.string.set_location)
                ), DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> toChooseLocation(cityName, xCoordinate, yCoordinate)
                    }
                }
            )
        }
    }

    private fun toChooseLocation(cityName: String, xCoordinate: Double?, yCoordinate: Double?) {
        parentFragmentManager.setFragmentResultListener(
            MapFragment.REQUEST_LOCATION,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
        val action = EventFragmentDirections.actionEventToMap(cityName)
        if (xCoordinate != null && yCoordinate != null) {
            action.xCoordinate = xCoordinate.toFloat()
            action.yCoordinate = yCoordinate.toFloat()
        }
        findNavController().navigate(action)
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            MapFragment.REQUEST_LOCATION -> {
                val location: MapLocation =
                    result.getParcelable(MapFragment.RESULT_LOCATION) ?: return
                presenter.updateEvent(
                    xCoordinate = location.latLong?.latitude,
                    yCoordinate = location.latLong?.longitude
                )
            }
        }
    }

    private fun editDescription(text: String?) {
        showEditTextDialog(
            getString(R.string.edit_description),
            text,
            getString(R.string.empty_description_event),
            callback = {
                presenter.updateEvent(description = it)
            }
        )
    }

    private fun showTime(startTime: String, endTime: String) {
        //меньше суток разницы 10 августа 10:00 - 14:00
        if (endTime.toLongTime(Const.DATE_FORMAT_TIME_ZONE) - startTime.toLongTime(Const.DATE_FORMAT_TIME_ZONE) < 1000 * 60 * 60 * 24) {
            val startDate = startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ON_CARD)
            val endTime = endTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_ONLY_TIME)
            val timeText = "$startDate - $endTime"
            start_date.text = timeText.toSpannable().hightlight(
                requireContext(),
                startDate.length - 5,
                timeText.length
            )
        } else { //больше суток разницы 10 августа 10:00 - 11 августа 15:00
            val startDate = startTime.parse(
                Const.DATE_FORMAT_TIME_ZONE,
                Const.DATE_FORMAT_OUTPUT_WITH_NAME_MONTH
            )
            val endDate = endTime.parse(
                Const.DATE_FORMAT_TIME_ZONE,
                Const.DATE_FORMAT_OUTPUT_WITH_NAME_MONTH
            )
            val timeText = "$startDate - $endDate"
            val indexOfFirst = timeText.indexOfFirst { it == ':' }
            val indexOfLast = timeText.indexOfLast { it == ':' }
            var spannable = timeText.toSpannable().hightlight(
                requireContext(),
                indexOfFirst - 2,
                indexOfFirst + 3
            )
            spannable = spannable.hightlight(
                requireContext(),
                indexOfLast - 2,
                indexOfLast + 3
            )
            start_date.text = spannable
        }
    }

    private fun showFullScreenImages(primaryImageContentUid: String, images: List<String>?) {
        context?.let {
            val allImages = mutableListOf(primaryImageContentUid)
            if (!images.isNullOrEmpty()) {
                allImages.addAll(images)
            }
            allImages.let {
                val action = EventFragmentDirections.actionFromEventToImageSlider(it.toTypedArray())
                action.eventUid = if (presenter.isAdministrator()) presenter.getEventUid() else null
                findNavController().navigate(action)
            }
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
            presenter.onClickInviteFriends()
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
                null,
                ChatOpeningInvocationType.Common(
                    title = eventName,
                    chatUid = chatUid,
                    isGroup = true
                )
            )
        )
    }
}