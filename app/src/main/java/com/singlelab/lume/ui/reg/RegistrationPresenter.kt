package com.singlelab.lume.ui.reg

import android.graphics.Bitmap
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.view.ValidationError
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.reg.interactor.RegistrationInteractor
import com.singlelab.lume.util.resize
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.person.ProfileRequest
import moxy.InjectViewState
import javax.inject.Inject


@InjectViewState
class RegistrationPresenter @Inject constructor(
    private val interactor: RegistrationInteractor,
    preferences: Preferences?
) : BasePresenter<RegistrationView>(preferences, interactor as BaseInteractor) {

    private val profileRequest = ProfileRequest()

    private var city: City? = null

    private var image: Bitmap? = null

    fun registration(name: String, age: Int, description: String) {
        val uid = AuthData.uid
        if (!uid.isNullOrEmpty() && city?.cityId != null && image != null) {
            viewState.showLoading(true)
            invokeSuspend {
                val imageStr = image!!.resize().toBase64()
                val miniImageStr = image!!.resize(200).toBase64()
                val profile = ProfileRequest(
                    name = name,
                    age = age,
                    description = description,
                    cityId = city!!.cityId,
                    image = imageStr,
                    miniImage = miniImageStr
                )
                try {
                    interactor.registration(profile)
                    preferences?.setAnon(false)
                    runOnMainThread {
                        viewState.onRegistration()
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun setCity(city: City) {
        this.city = city
        viewState.showCity(city.cityName)
    }

    fun addImage(bitmap: Bitmap?) {
        this.image = bitmap
        viewState.showImage(bitmap)
    }

    fun validation(
        name: String?,
        age: String?,
        description: String?
    ): ValidationError? {
        return when {
            name.isNullOrEmpty() -> ValidationError.EMPTY_NAME
            description.isNullOrEmpty() -> ValidationError.EMPTY_DESCRIPTION
            age.isNullOrEmpty() -> ValidationError.EMPTY_AGE
            image == null -> ValidationError.EMPTY_PHOTO
            city == null -> ValidationError.EMPTY_CITY
            else -> null
        }
    }

    fun setLogin(login: String) {
        profileRequest.login = login
    }

    fun setName(name: String) {
        profileRequest.name = name
    }

    fun setAge(age: Int) {
        profileRequest.age = age
    }

    fun setDescription(description: String) {
        profileRequest.description = description
    }

    fun saveInputs() {
        viewState.showName(profileRequest.name)
        if (profileRequest.age != null) {
            viewState.showAge(profileRequest.age!!)
        }
        viewState.showDescription(profileRequest.description)
    }
}