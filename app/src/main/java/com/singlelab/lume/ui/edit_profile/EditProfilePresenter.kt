package com.singlelab.lume.ui.edit_profile


import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.city.City
import com.singlelab.lume.model.profile.NewProfile
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.edit_profile.interactor.EditProfileInteractor
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditProfilePresenter @Inject constructor(
    private val interactor: EditProfileInteractor,
    preferences: Preferences?
) : BasePresenter<EditProfileView>(preferences, interactor as BaseInteractor) {

    private var newProfile = NewProfile()

    fun setProfile(profile: Profile) {
        newProfile = NewProfile(profile)
        viewState.showProfile(newProfile)
    }

    fun setCity(city: City?) {
        city?.let {
            newProfile.city = city
            viewState.showProfile(newProfile)
        }
    }

    fun setName(name: String) {
        newProfile.name = name
    }

    fun setAge(age: String) {
        try {
            newProfile.age = age.toInt()
        } catch (e: Exception) {
            viewState.showError("Можно вводить только цифры")
        }
    }

    fun setDescription(description: String) {
        newProfile.description = description
    }

    fun updateProfile() {
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                interactor.updateProfile(newProfile)
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.onCompleteUpdate()
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }

    }
}