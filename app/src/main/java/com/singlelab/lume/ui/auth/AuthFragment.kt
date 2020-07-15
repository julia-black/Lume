package com.singlelab.lume.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment(), AuthView {

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
        activity?.title = "Авторизация"
        setListeners()
    }

    private fun setListeners() {
        button_send_code.setOnClickListener {
            authPresenter.onClickSendCode(edit_text_phone.text.toString())
        }
    }

    override fun onCodeSend() {
        Toast.makeText(context, "Код успешно отправлен", Toast.LENGTH_LONG).show()
    }

    override fun showLoading(isShow: Boolean) {
        super.showLoadingView(isShow)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}