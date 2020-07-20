package com.singlelab.lume.ui.reg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_registration.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(), RegistrationView {

    @Inject
    lateinit var daggerPresenter: RegistrationPresenter

    @InjectPresenter
    lateinit var presenter: RegistrationPresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.registration)
        setListeners()
    }

    private fun setListeners() {
        button_registration.setOnClickListener {
            presenter.registration(
                name.text.toString(),
                age.text.toString().toInt(),
                description.text.toString()
            )
        }
    }

    override fun onRegistration() {
        Navigation.createNavigateOnClickListener(R.id.action_registration_to_my_profile)
            .onClick(view)
    }
}