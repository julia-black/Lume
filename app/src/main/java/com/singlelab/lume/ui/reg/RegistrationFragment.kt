package com.singlelab.lume.ui.reg

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.view.ValidationError
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.view.input.InputView
import com.singlelab.lume.ui.view.input.OnInvalidStringsListener
import com.singlelab.lume.util.getBitmap
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_registration.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(), RegistrationView,
    OnActivityResultListener, OnInvalidStringsListener {

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
        initInputViews()
        setListeners()
    }

    override fun onRegistration() {
        findNavController().popBackStack()
        findNavController().popBackStack()
        findNavController().navigate(R.id.my_profile)
    }

    override fun showLogin(login: String?) {
        layout_login.setText(login)
    }

    override fun showName(name: String?) {
        layout_name.setText(name)
    }

    override fun showAge(age: Int) {
        layout_age.setText(age.toString())
    }

    override fun showDescription(description: String?) {
        layout_description.setText(description)
    }

    override fun showCity(cityName: String) {
        text_city.text = cityName
    }

    override fun showImage(bitmap: Bitmap?) {
        image.setImageBitmap(bitmap)
    }

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val bitmap = result.uri.getBitmap(activity?.contentResolver)
                presenter.addImage(bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showError(getString(R.string.error_pick_image))
            }
        }
    }

    override fun onInvalidString(view: InputView) {
        view.setError(getString(R.string.login_hint))
    }

    override fun onCorrectString(view: InputView) {
        view.setWarning(getString(R.string.login_hint))
    }

    private fun initInputViews() {
        layout_login.apply {
            setHint(getString(R.string.login))
            setWarning(getString(R.string.login_hint))
            setImeAction(EditorInfo.IME_ACTION_NEXT)
            setOnEditorListener {
                if (it == EditorInfo.IME_ACTION_NEXT) {
                    runOnMainThread {
                        this@RegistrationFragment.view?.clearFocus()
                        layout_name.requestEditTextFocus()
                    }
                }
                return@setOnEditorListener false
            }
            setSingleLine()
            setMaxLength(25)
            val pattern = Regex(Const.REGEX_LOGIN)
            setInvalidStringsListener(pattern, this@RegistrationFragment)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.setLogin(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        layout_name.apply {
            setHint(getString(R.string.name))
            setImeAction(EditorInfo.IME_ACTION_NEXT)
            setOnEditorListener {
                if (it == EditorInfo.IME_ACTION_NEXT) {
                    runOnMainThread {
                        this@RegistrationFragment.view?.clearFocus()
                        layout_age.requestEditTextFocus()
                    }
                }
                return@setOnEditorListener false
            }
            setSingleLine()
            setMaxLength(25)
            setWarning(getString(R.string.max_length, 25))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    presenter.setName(s.toString())
                }
            })
        }
        layout_age.apply {
            setHint(getString(R.string.age))
            setMaxLength(2)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            setImeAction(EditorInfo.IME_ACTION_DONE)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    try {
                        val intAge = s.toString().toInt()
                        presenter.setAge(intAge)
                    } catch (e: Exception) {
                    }
                }
            })
        }
        layout_description.apply {
            setHint(getString(R.string.about_yourself))
            setLines(5)
            disableLineBreaks()
            setMaxLines(5)
            setMaxLength(128)
            setWarning(getString(R.string.max_length, 128))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    presenter.setDescription(s.toString())
                }
            })
        }
    }

    private fun setListeners() {
        card_image.setOnClickListener {
            presenter.saveInputs()
            onClickChangeImage()
        }
        button_upload_image.setOnClickListener {
            presenter.saveInputs()
            onClickChangeImage()
        }
        text_city.setOnClickListener {
            toCityChoose()
        }
        button_registration.setOnClickListener {
            val validationError = validation()
            if (validationError == null) {
                presenter.registration(
                    layout_login.getText(),
                    layout_name.getText(),
                    layout_age.getText().toInt(),
                    layout_description.getText()
                )
            } else {
                showError(getString(validationError.titleResId))
            }
        }
        parentFragmentManager.setFragmentResultListener(
            CitiesFragment.REQUEST_CITY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    private fun toCityChoose() {
        presenter.saveInputs()
        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationToCities())
    }

    private fun validation(): ValidationError? {
        return presenter.validation(
            layout_login.getText(),
            layout_name.getText(),
            layout_age.getText(),
            layout_description.getText()
        )
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CitiesFragment.REQUEST_CITY) {
            val city: City = result.getParcelable(CitiesFragment.RESULT_CITY) ?: return
            presenter.setCity(city)
        }
    }
}