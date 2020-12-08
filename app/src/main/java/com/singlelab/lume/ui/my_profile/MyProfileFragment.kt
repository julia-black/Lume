package com.singlelab.lume.ui.my_profile

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.model.view.PagerTab
import com.singlelab.lume.ui.view.pager.*
import com.singlelab.lume.ui.view.pager.listener.OnFriendsClickListener
import com.singlelab.lume.ui.view.pager.listener.OnSettingsClickListener
import com.singlelab.lume.ui.view.person_short.OnPersonShortClickListener
import com.singlelab.lume.util.PluralsUtil
import com.singlelab.lume.util.generateImageLink
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
    OnPersonShortClickListener, OnSettingsClickListener, OnFriendsClickListener {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

    private lateinit var settingsView: SettingsView

    private lateinit var friendsView: FriendsView

    private lateinit var badgesView: BadgesView

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
        if (AuthData.isAnon) {
            navigateToAuth()
        } else {
            presenter.loadProfile(presenter.profile == null)
        }
    }

    private fun initViewPager() {
        context?.let {
            friendsView = FriendsView(it)
            friendsView.setFriendsListener(this, this)

            badgesView = BadgesView(it)

            settingsView = SettingsView(it)
            settingsView.setSettingsListener(this)
        }
        val views = mutableListOf<PagerTabView>(friendsView, badgesView, settingsView)
        view_pager.apply {
            adapter = PagerAdapter(views)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = views[position].getTitle()
            view_pager.setCurrentItem(tab.position, true)
        }.attach()
        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    PagerTab.FRIENDS.position -> {
                        friendsView.showLoading(true)
                        presenter.loadFriends()
                    }
                    PagerTab.BADGES.position -> {
                        badgesView.showLoading(true)
                        presenter.loadBadges()
                    }
                }
            }
        })
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
            loadImage(profile.imageContentUid)
        }
        image.setOnClickListener {
            if (profile.imageContentUid == null) {
                onClickChangeImage()
            } else {
                onClickImage(profile.imageContentUid)
            }
        }
        button_edit_profile.setOnClickListener {
            presenter.profile?.let {
                findNavController().navigate(
                    MyProfileFragmentDirections.actionMyProfileToEditProfile(it)
                )
            }
        }
        initViewPager()
    }

    override fun onLoadedFriends(friends: List<Person>?) {
        friendsView.setFriends(friends)
        (view_pager.adapter as PagerAdapter?)?.updateFriendsView(friendsView)
    }

    override fun onLoadedBadges(badges: List<Badge>) {
        badgesView.setBadges(badges)
        (view_pager.adapter as PagerAdapter?)?.updateBadgesView(badgesView)
    }

    override fun showNewBadge(hasNewBadges: Boolean) {
        if (hasNewBadges) {
            val badge = tab_layout.getTabAt(1)?.orCreateBadge
            badge?.backgroundColor =
                ContextCompat.getColor(requireContext(), R.color.colorNotification)
        } else {
            tab_layout.getTabAt(1)?.removeBadge()
        }
    }

    override fun showLoadingFriends(isShow: Boolean) {
        friendsView.showLoading(isShow)
    }

    override fun showNewYearView() {
        context?.let {
            view_shape.background = ContextCompat.getDrawable(it, R.drawable.shape_profile_new_year)
        }
    }

    override fun navigateToAuth() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.auth)
    }

    override fun loadImage(imageUid: String?) {
        imageUid?.let {
            Glide.with(this)
                .load(imageUid.generateImageLink())
                .thumbnail(0.1f)
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
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun onPersonClick(personUid: String) {
        findNavController().navigate(MyProfileFragmentDirections.actionMyProfileToPerson(personUid))
    }

    override fun onInstagramClick() {
        openBrowser(getString(R.string.url_instagram))
    }

    override fun onFeedbackClick() {
        findNavController().navigate(MyProfileFragmentDirections.actionMyProfileToFeedback())
    }

    override fun onAgreementClick() {
        openBrowser(getString(R.string.url_agreement))
    }

    override fun onAboutDeveloperClick() {
        openBrowser(getString(R.string.url_about_developer))
    }

    override fun onLogoutClick() {
        presenter.logout()
    }

    override fun onSearchFriendsClick() {
        toFriends(true)
    }

    override fun onNewFriendsClick() {
        toFriends(true)
    }

    override fun onInviteFriendsClick() {
        shareText(Const.STORE_URL)
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
            val links = listOf(imageContentUid)
            findNavController().navigate(
                MyProfileFragmentDirections.actionMyProfileToImageSlider(
                    links.toTypedArray()
                )
            )
        }
    }

    private fun toFriends(isSearch: Boolean = false) {
        val action = MyProfileFragmentDirections.actionMyProfileToFriends()
        action.isSearch = isSearch
        findNavController().navigate(action)
    }

    fun onBackPressed() {
        callbackBackPressed.remove()
    }
}