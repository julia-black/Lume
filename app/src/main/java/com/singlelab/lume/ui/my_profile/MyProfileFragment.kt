package com.singlelab.lume.ui.my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singlelab.lume.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : MvpAppCompatFragment(), MyProfileView {

    @Inject
    lateinit var hiltPresenter: MyProfilePresenter

    @InjectPresenter
    lateinit var myProfilePresenter: MyProfilePresenter

    @ProvidePresenter
    fun provideMyProfilePresenter() = hiltPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_notifications.text = "is my profile"
    }

    override fun showProfile() {
        text_notifications.text = "show profile"
    }
}