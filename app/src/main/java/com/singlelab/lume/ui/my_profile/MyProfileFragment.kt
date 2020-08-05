package com.singlelab.lume.ui.my_profile

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.custom.sliderimage.logic.SliderImage
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.model.view.PagerTab
import com.singlelab.lume.ui.view.image_person.OnPersonImageClickListener
import com.singlelab.lume.ui.view.pager.FriendsView
import com.singlelab.lume.ui.view.pager.SettingsView
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import com.singlelab.lume.ui.view.pager.listener.OnSettingsClickListener
import com.singlelab.lume.util.generateImageLinkForPerson
import com.singlelab.lume.util.getBitmap
import com.singlelab.net.model.auth.AuthData
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class MyProfileFragment : BaseFragment(), MyProfileView, OnActivityResultListener,
    OnPersonImageClickListener, OnSettingsClickListener, OnFriendsClickListener {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

    private lateinit var settingsView: SettingsView

    private lateinit var friendsView: FriendsView

    private val callbackBackPressed: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.image)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_my_profile_to_auth))

        context?.let {
            settingsView = SettingsView(it)
            settingsView.setSettingsListener(this)
            friendsView = FriendsView(it)
            friendsView.setFriendsListener(this, this)
        }

        if (AuthData.isAnon) {
            navigateToAuth()
        } else {
            presenter.loadProfile(presenter.profile == null)
        }
    }

    override fun showProfile(profile: Profile) {
        name.text = profile.name
        login.text = profile.login
        age.text = resources.getQuantityString(R.plurals.age_plurals, profile.age, profile.age)
        description.text = profile.description
        city.text = profile.cityName
        if (!profile.imageContentUid.isNullOrEmpty()) {
            loadImage(profile.imageContentUid)
        } else {
            image.setImageDrawable(context?.getDrawable(R.drawable.ic_profile))
        }
        image.setOnClickListener {
            if (profile.imageContentUid == null) {
                onClickChangeImage()
            } else {
                onClickImage(profile.imageContentUid)
            }
        }
        selectTab(presenter.selectedTab)
        setTabListeners()
        friendsView.setFriends(profile.friends)
    }

    private fun onClickImage(imageContentUid: String) {
        showListDialog(
            getString(R.string.choose_action),
            arrayOf(
                getString(R.string.show_image),
                getString(R.string.change_image)
            ), DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> showFullScreenImage(imageContentUid)
                    1 -> onClickChangeImage()
                }
            }
        )
    }

    private fun showFullScreenImage(imageContentUid: String) {
        context?.let {
            val links = listOf(imageContentUid.generateImageLinkForPerson())
            SliderImage.openfullScreen(it, links, 0)
        }
    }

    override fun navigateToAuth() {
        Navigation.createNavigateOnClickListener(R.id.action_my_profile_to_auth)
            .onClick(view)
    }

    override fun loadImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLinkForPerson())
                .into(image)
        }
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val bitmap = result.uri.getBitmap(activity?.contentResolver)
                presenter.updateImageProfile(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, getString(R.string.error_pick_image), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(MyProfileFragmentDirections.actionMyProfileToPerson(personUid))
    }

    override fun onPersonInfoClick() {
        presenter.profile?.let {
            findNavController().navigate(MyProfileFragmentDirections.actionMyProfileToEditProfile(it))
        }
    }

    override fun onLogoutClick() {
        presenter.logout()
    }

    override fun onSearchFriendsClick() {
        toFriends(true)
    }

    private fun toFriends(isSearch: Boolean = false) {
        val action = MyProfileFragmentDirections.actionMyProfileToFriends()
        action.isSearch = isSearch
        findNavController().navigate(action)
    }

    private fun selectTab(tab: PagerTab) {
        presenter.selectedTab = tab
        when (tab) {
            PagerTab.FRIENDS -> {
                showFriends()
            }
            PagerTab.BADGES -> {
                showBadges()
            }
            PagerTab.SETTINGS -> {
                showSettings()
            }
        }
    }

    private fun setTabListeners() {
        text_tab_one.setOnClickListener {
            selectTab(PagerTab.FRIENDS)
        }
        text_tab_two.setOnClickListener {
            selectTab(PagerTab.BADGES)
        }
        text_tab_three.setOnClickListener {
            selectTab(PagerTab.SETTINGS)
        }
    }

    private fun showFriends() {
        context?.let {
            text_tab_one.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorWhite)
            text_tab_one.setTextColor(ContextCompat.getColor(it, R.color.colorPrimaryDark))

            text_tab_two.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_two.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))

            text_tab_three.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_three.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))
        }

        card_content.removeAllViews()
        card_content.addView(
            friendsView,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun showBadges() {
        context?.let {
            text_tab_one.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_one.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))

            text_tab_two.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorWhite)
            text_tab_two.setTextColor(ContextCompat.getColor(it, R.color.colorPrimaryDark))

            text_tab_three.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_three.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))
        }

        card_content.removeAllViews()
    }

    private fun showSettings() {
        context?.let {
            text_tab_one.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_one.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))

            text_tab_two.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorPrimary)
            text_tab_two.setTextColor(ContextCompat.getColor(it, R.color.colorWhite))

            text_tab_three.backgroundTintList =
                ContextCompat.getColorStateList(it, R.color.colorWhite)
            text_tab_three.setTextColor(ContextCompat.getColor(it, R.color.colorPrimaryDark))
        }

        card_content.removeAllViews()
        card_content.addView(
            settingsView,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun onBackPressed() {
        frame_container.visibility = View.GONE
        callbackBackPressed.remove()
    }
}