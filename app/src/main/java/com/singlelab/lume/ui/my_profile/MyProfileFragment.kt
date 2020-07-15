package com.singlelab.lume.ui.my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.singlelab.lume.R
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : MvpAppCompatFragment(), MyProfileView {

    @Inject
    lateinit var daggerPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var myProfilePresenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Мой профиль"
    }

    override fun showProfile() {
    }

    override fun navigateToAuth() {
        activity?.let {
            myProfilePresenter.navigateToAuth(
                Navigation.findNavController(
                    it,
                    R.id.nav_host_fragment
                )
            )
        }
    }
}