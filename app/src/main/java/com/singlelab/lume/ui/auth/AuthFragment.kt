package com.singlelab.lume.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.singlelab.lume.MainActivity
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnBackPressListener
import com.singlelab.lume.util.maskPhone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment(), AuthView, OnBackPressListener {

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
        setListeners()
        layout_phone.hint = ""
        edit_text_phone.setCustomHint("")
        edit_text_phone.requestFocus()
    }

    private fun setListeners() {
        edit_text_phone.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onClickSendCode()
                }
            }
            false
        }
        edit_text_code.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onClickAuth()
                }
            }
            false
        }
        button_send_code.setOnClickListener {
            onClickSendCode()
        }
        button_auth.setOnClickListener {
            onClickAuth()
        }
    }

    private fun onClickAuth() {
        hideKeyboard()
        (activity as MainActivity).setBackPressListener(null)
        presenter.onClickAuth(edit_text_code.text.toString())
    }

    private fun onClickSendCode() {
        hideKeyboard()
        edit_text_phone.fixHintsForMeizu(edit_text_phone as TextInputEditText, edit_text_phone)
        if (edit_text_phone.isValid) {
            context?.let {
                presenter.onClickSendCode(
                    edit_text_phone.unmaskText,
                    NotificationManagerCompat.from(it).areNotificationsEnabled()
                )
            }

        } else {
            layout_phone.error = "Некорректный номер"
        }
    }

    override fun onCodeSend(phone: String, isPushCode: Boolean) {
        (activity as MainActivity).setBackPressListener(this)
        text_info.visibility = View.VISIBLE
        if (isPushCode) {
            text_info.text = getString(R.string.push_code_send)
        } else {
            text_info.text = getString(R.string.sms_code_send, phone.maskPhone())
        }
        layout_code.visibility = View.VISIBLE
        edit_text_code.setText("")
        button_auth.visibility = View.VISIBLE

        layout_phone.visibility = View.INVISIBLE
        button_send_code.visibility = View.INVISIBLE
    }

    override fun toProfile() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.my_profile)
    }

    override fun toRegistration() {
        showInputPhone()
        (activity as MainActivity).setBackPressListener(null)
        findNavController().navigate(AuthFragmentDirections.actionAuthToRegistration())
    }

    override fun onBackPressed() {
        showInputPhone()
        (activity as MainActivity).setBackPressListener(null)
    }

    private fun showInputPhone() {
        text_info.visibility = View.GONE
        layout_code.visibility = View.INVISIBLE
        button_auth.visibility = View.INVISIBLE

        layout_phone.visibility = View.VISIBLE
        button_send_code.visibility = View.VISIBLE
    }
}