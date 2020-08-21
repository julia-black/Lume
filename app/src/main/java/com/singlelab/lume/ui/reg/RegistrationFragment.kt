package com.singlelab.lume.ui.reg

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.singlelab.lume.R
import com.singlelab.lume.base.BaseFragment
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.view.ValidationError
import com.singlelab.lume.ui.cities.CitiesFragment
import com.singlelab.lume.util.getBitmap
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_registration.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(), RegistrationView, OnActivityResultListener {

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
        setListeners()
    }

    override fun onRegistration() {
        Navigation.createNavigateOnClickListener(R.id.action_registration_to_my_profile)
            .onClick(view)
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

    private fun setListeners() {
        image.setOnClickListener {
            onClickChangeImage()
        }
        text_city.setOnClickListener {
            toCityChoose()
        }
        button_registration.setOnClickListener {
            val validationError = validation()
            if (validationError == null) {
                presenter.registration(
                    login.text.toString(),
                    name.text.toString(),
                    age.text.toString().toInt(),
                    description.text.toString()
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
        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationToCities())
    }

    private fun validation(): ValidationError? {
        return presenter.validation(
            login.text.toString(),
            name.text.toString(),
            description.text.toString(),
            age.text.toString()
        )
    }

    private fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CitiesFragment.REQUEST_CITY) {
            val city: City = result.getParcelable(CitiesFragment.RESULT_CITY) ?: return
            presenter.setCity(city)
        }
    }
}