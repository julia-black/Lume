package com.singlelab.lume.ui.edit_profile

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
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.profile.NewProfile
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.ui.view.input.InputView
import com.singlelab.lume.ui.view.input.OnInvalidStringsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : BaseFragment(), EditProfileView, OnInvalidStringsListener {

    @Inject
    lateinit var daggerPresenter: EditProfilePresenter

    @InjectPresenter
    lateinit var presenter: EditProfilePresenter

    @ProvidePresenter
    fun providePresenter() = daggerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.setProfile(EditProfileFragmentArgs.fromBundle(it).profile)
        }
        setListeners()
    }

    override fun showLogin(login: String?) {
        this.login.setText(login)
    }

    override fun showName(name: String?) {
        this.name.setText(name)
    }

    override fun showAge(age: Int) {
        this.age.setText(age.toString())
    }

    override fun showDescription(description: String?) {
        this.description.setText(description)
    }

    override fun showProfile(profile: NewProfile) {
        login.setText(profile.login)
        name.setText(profile.name)
        age.setText(profile.age.toString())
        text_city.text = profile.city?.cityName
        description.setText(profile.description)
        initInputViews()
    }

    override fun onCompleteUpdate() {
        findNavController().popBackStack()
    }

    override fun onInvalidString(view: InputView) {
        view.setError(getString(R.string.login_hint))
    }

    override fun onCorrectString(view: InputView) {
        view.setWarning(getString(R.string.login_hint))
    }

    private fun initInputViews() {
        login.apply {
            setHint(getString(R.string.login))
            setWarning(getString(R.string.login_hint))
            setImeAction(EditorInfo.IME_ACTION_NEXT)
            setOnEditorListener {
                if (it == EditorInfo.IME_ACTION_NEXT) {
                    runOnMainThread {
                        this@EditProfileFragment.view?.clearFocus()
                        name.requestEditTextFocus()
                    }
                }
                return@setOnEditorListener false
            }
            setSingleLine()
            setMaxLength(25)
            val pattern = Regex(Const.REGEX_LOGIN)
            login.setInvalidStringsListener(pattern, this@EditProfileFragment)
        }
        name.apply {
            setHint(getString(R.string.name))
            setImeAction(EditorInfo.IME_ACTION_NEXT)
            setOnEditorListener {
                if (it == EditorInfo.IME_ACTION_NEXT) {
                    runOnMainThread {
                        this@EditProfileFragment.view?.clearFocus()
                        age.requestEditTextFocus()
                    }
                }
                return@setOnEditorListener false
            }
            setSingleLine()
            setMaxLength(25)
            setWarning(getString(R.string.max_length, 25))
        }
        age.apply {
            setHint(getString(R.string.age))
            setMaxLength(2)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            setImeAction(EditorInfo.IME_ACTION_DONE)
        }
        description.apply {
            setHint(getString(R.string.about_yourself))
            setLines(5)
            disableLineBreaks()
            setMaxLines(5)
            setMaxLength(128)
            setWarning(getString(R.string.max_length, 128))
        }
    }

    private fun setListeners() {
        login.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.setLogin(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.setName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        age.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val intAge = s.toString().toInt()
                    presenter.setAge(intAge)
                } catch (e: Exception) {
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.setDescription(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        text_city.setOnClickListener {
            toChooseCity()
        }
        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        button_update.setOnClickListener {
            presenter.updateProfile()
        }
        parentFragmentManager.setFragmentResultListener(
            CitiesFragment.REQUEST_CITY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CitiesFragment.REQUEST_CITY) {
            val city: City? = result.getParcelable(CitiesFragment.RESULT_CITY)
            presenter.setCity(city)
        }
    }

    private fun toChooseCity() {
        presenter.saveInputs()
        findNavController().navigate(EditProfileFragmentDirections.actionEditProfileToCities())
    }
}