package com.singlelab.lume.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.util.generateImageLinkForPerson
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
        name_age.text = "${profile.name}, ${profile.age}"
        description.text = profile.description
        city.text = profile.cityName
        if (!profile.imageContentUid.isNullOrEmpty()) {
            showImage(profile.imageContentUid)
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
    }

    override fun onAddedToFriends() {
        button_add_to_friends.visibility = View.GONE
        button_remove_from_friends.visibility = View.VISIBLE
    }

    override fun onRemovedFromFriends() {
        button_add_to_friends.visibility = View.VISIBLE
        button_remove_from_friends.visibility = View.GONE
    }

    private fun showImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLinkForPerson())
                .into(image)
        }
    }
}