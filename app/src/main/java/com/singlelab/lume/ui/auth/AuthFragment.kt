package com.singlelab.lume.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singlelab.lume.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : MvpAppCompatFragment(), AuthView {

    @Inject
    lateinit var daggerPresenter: AuthPresenter

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    @ProvidePresenter
    fun provideAuthPresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_auth.text = "is auth"
        activity?.title = "Авторизация"
    }
}