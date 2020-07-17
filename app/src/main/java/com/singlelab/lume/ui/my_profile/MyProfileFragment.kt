package com.singlelab.lume.ui.my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.singlelab.data.model.profile.Profile
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnToolbarListener
import dagger.hilt.android.AndroidEntryPoint
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : BaseFragment(), MyProfileView, OnToolbarListener {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.title = getString(R.string.title_my_profile)
            navController = Navigation.findNavController(it, R.id.nav_host_fragment)
        }
        view.findViewById<ImageView>(R.id.image)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_my_profile_to_navigation_auth))
    }

    override fun onDestroy() {
        (activity as MainActivity?)?.showLogoutInToolbar(false)
        super.onDestroy()
    }

    override fun showProfile(profile: Profile) {
        (activity as MainActivity?)?.showLogoutInToolbar(true)
    }

    override fun navigateToAuth() {
        Navigation.createNavigateOnClickListener(R.id.action_navigation_my_profile_to_navigation_auth)
            .onClick(view)
//        navController?.let {
//            presenter.navigateToAuth(it)
//        }
    }

    override fun onClickLogout() {
        navController?.let {
            presenter.logout()
        }
    }
}