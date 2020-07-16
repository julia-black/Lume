package com.singlelab.lume.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.util.maskPhone
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
    lateinit var presenter: AuthPresenter

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
        layout_phone.hint = ""
        edit_text_phone.setCustomHint("")
    }

    private fun setListeners() {
        button_send_code.setOnClickListener {
            edit_text_phone.fixHintsForMeizu(edit_text_phone as TextInputEditText, edit_text_phone)
            if (edit_text_phone.isValid) {
                presenter.onClickSendCode(edit_text_phone.unmaskText)
            } else {
                layout_phone.error = "Некорректный номер"
            }
        }
        button_auth.setOnClickListener {
            presenter.onClickAuth(edit_text_code.text.toString())
        }
    }

    override fun onCodeSend(phone: String) {
        text_info.visibility = View.VISIBLE
        text_info.text = "СМС-код отправлен на номер ${phone.maskPhone()}"
        layout_code.visibility = View.VISIBLE
        button_auth.visibility = View.VISIBLE

        layout_phone.visibility = View.INVISIBLE
        button_send_code.visibility = View.INVISIBLE
    }

    override fun onAuth() {
        activity?.let {
            presenter.navigateToMyProfile(
                Navigation.findNavController(
                    it,
                    R.id.nav_host_fragment
                )
            )
        }
    }
}