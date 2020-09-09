package com.singlelab.lume.ui.reg

import android.graphics.Bitmap
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.view.ValidationError
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.reg.interactor.RegistrationInteractor
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

    fun registration(login: String, name: String, age: Int, description: String) {
        val uid = AuthData.uid
        if (!uid.isNullOrEmpty() && city?.cityId != null && image != null) {
            viewState.showLoading(true)
            invokeSuspend {
                val profile = ProfileRequest(
                    login = login,
                    name = name,
                    age = age,
                    description = description,
                    cityId = city!!.cityId,
                    image = image!!.toBase64()
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
        login: String?,
        name: String?,
        age: String?,
        description: String?
    ): ValidationError? {
        return when {
            login.isNullOrEmpty() -> ValidationError.EMPTY_LOGIN
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
        viewState.showLogin(profileRequest.login)
        viewState.showName(profileRequest.name)
        if (profileRequest.age != null) {
            viewState.showAge(profileRequest.age!!)
        }
        viewState.showDescription(profileRequest.description)
    }
}