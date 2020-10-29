package com.singlelab.lume.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.model.view.ToastType
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
import com.singlelab.lume.util.PluralsUtil
import com.singlelab.lume.util.generateImageLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_person.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class PersonFragment : BaseFragment(), PersonView {

    @Inject
    lateinit var daggerPresenter: PersonPresenter

    @InjectPresenter
    lateinit var presenter: PersonPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.loadProfile(PersonFragmentArgs.fromBundle(it).personUid)
        }
    }

    override fun showProfile(profile: Profile) {
        val age = PluralsUtil.getString(
            profile.age,
            "год",
            "года",
            "года",
            "года",
            "лет"
        )
        name.text = "${profile.name}, $age"
        login.text = "@${profile.login}"
        description.text = profile.description
        city.text = profile.cityName
        if (!profile.imageContentUid.isNullOrEmpty()) {
            showImage(profile.imageContentUid)
            image.setOnClickListener {
                showFullScreenImage(profile.imageContentUid)
            }
        }
        if (profile.isFriend) {
            button_action_friend.text = getString(R.string.remove_from_friends)
        } else {
            button_action_friend.text = getString(R.string.add_to_friends)
        }

        button_action_friend.setOnClickListener {
            if (profile.isFriend) {
                presenter.removeFromFriends(profile.personUid)
            } else {
                presenter.addToFriends(profile.personUid)
            }
        }
        button_chat.setOnClickListener {
            toChat(profile.name, profile.personUid)
        }
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        button_report_person.setOnClickListener {
            showReport()
        }
    }

    override fun showSuccessReport() {
        hideKeyboard()
        showSnackbar(getString(R.string.report_send), ToastType.SUCCESS)
    }

    private fun showReport() {
        showEditTextDialog(
            title = getString(R.string.enter_reason_report),
            emptyText = getString(R.string.empty_reason),
            callback = {
                presenter.sendReport(it)
            })
    }

    private fun toChat(title: String, personUid: String) {
        findNavController().navigate(
            PersonFragmentDirections.actionPersonToChat(
                null,
                ChatOpeningInvocationType.Person(
                    title = title,
                    personUid = personUid
                )
            )
        )
    }

    private fun showFullScreenImage(imageContentUid: String) {
        context?.let {
            val links = listOf(imageContentUid)
            findNavController().navigate(PersonFragmentDirections.actionPersonToImageSlider(links.toTypedArray()))
        }
    }

    private fun showImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLink())
                .thumbnail(0.1f)
                .into(image)
        }
    }
}