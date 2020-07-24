package com.singlelab.lume.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.OnlyForAuthFragments
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.util.generateImageLink
import com.singlelab.lume.util.parse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : BaseFragment(), EventView, OnlyForAuthFragments {

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
        activity?.title = getString(R.string.title_event)
        arguments?.let {
            presenter.loadEvent(EventFragmentArgs.fromBundle(it).eventUid)
        }
        setListeners()
    }

    override fun showEvent(event: Event) {
        title.text = event.name
        start_date.text =
            event.startTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)
        end_date.text = event.endTime.parse(Const.DATE_FORMAT_TIME_ZONE, Const.DATE_FORMAT_OUTPUT)
        description.text = event.description
        val eventType = EventType.findById(event.type)
        eventType?.let {
            context?.let { safeContext ->
                type.text = getString(it.titleRes)
                type.backgroundTintList = ContextCompat.getColorStateList(safeContext, it.colorRes)
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
        event.administrator?.let {
            administrator.text = it.name
            it.imageContentUid?.let { imageUid ->
                showImage(image_administrator, imageUid)
            }
        }
    }

    override fun toMyProfile() {
        findNavController().navigate(R.id.action_event_to_my_profile)
    }

    override fun toProfile(personUid: String) {
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
            presenter.event?.eventUid?.let { eventUid ->
                toChat(eventUid)
            }
        }
        button_invite.setOnClickListener {
            presenter.event?.eventUid?.let { eventUid ->
                toInvite(eventUid)
            }
        }
    }

    private fun toChat(eventUid: String) {
        //todo переход в чат события
    }

    private fun toInvite(eventUid: String) {
        val action = EventFragmentDirections.actionEventToFriends()
        action.eventUid = eventUid
        findNavController().navigate(action)

    }

    private fun showImage(imageView: ImageView, imageUid: String) {
        Glide.with(this)
            .load(imageUid.generateImageLink())
            .into(imageView)
    }
}