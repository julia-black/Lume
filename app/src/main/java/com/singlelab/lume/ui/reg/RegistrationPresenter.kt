package com.singlelab.lume.ui.reg

import android.graphics.Bitmap
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.city.City
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
                    cityId = city!!.cityId
                )
                try {
                    interactor.registration(profile)
                    interactor.addPhoto(image!!.toBase64())
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

    fun validation(login: String?, name: String?, age: String?, description: String?): Boolean {
        return !(login.isNullOrEmpty() ||
                name.isNullOrEmpty() ||
                age.isNullOrEmpty() ||
                description.isNullOrEmpty() ||
                image == null
                )
    }
}