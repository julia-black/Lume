package com.singlelab.lume.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.custom.sliderimage.logic.SliderImage
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.ui.chat.common.ChatOpeningInvocationType
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
        name.text = profile.name
        login.text = "@${profile.login}"
        age.text = resources.getQuantityString(R.plurals.age_plurals, profile.age, profile.age)
        description.text = profile.description
        city.text = profile.cityName
        if (!profile.imageContentUid.isNullOrEmpty()) {
            showImage(profile.imageContentUid)
            image.setOnClickListener {
                showFullScreenImage(profile.imageContentUid)
            }
        } else {
            image.setImageDrawable(context?.getDrawable(R.drawable.ic_profile))
        }
        if (profile.isFriend) {
            button_add_to_friends.visibility = View.GONE
            button_remove_from_friends.visibility = View.VISIBLE
        } else {
            button_add_to_friends.visibility = View.VISIBLE
            button_remove_from_friends.visibility = View.GONE
        }

        button_add_to_friends.setOnClickListener {
            presenter.addToFriends(profile.personUid)
        }
        button_remove_from_friends.setOnClickListener {
            presenter.removeFromFriends(profile.personUid)
        }
        button_chat.setOnClickListener {
            toChat(profile.name, profile.personUid)
        }
    }

    override fun onAddedToFriends() {
        button_add_to_friends.visibility = View.GONE
        button_remove_from_friends.visibility = View.VISIBLE
    }

    override fun onRemovedFromFriends() {
        button_add_to_friends.visibility = View.VISIBLE
        button_remove_from_friends.visibility = View.GONE
    }

    private fun toChat(title: String, personUid: String) {
        findNavController().navigate(
            PersonFragmentDirections.actionPersonToChat(
                ChatOpeningInvocationType.Person(
                    title = title,
                    personUid = personUid
                )
            )
        )
    }

    private fun showFullScreenImage(imageContentUid: String) {
        context?.let {
            val links = listOf(imageContentUid.generateImageLink())
            SliderImage.openfullScreen(it, links, 0)
        }
    }

    private fun showImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLink())
                .into(image)
        }
    }
}