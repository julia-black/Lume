package com.singlelab.lume.model.profile

import com.singlelab.lume.model.city.City
import com.singlelab.net.model.person.ProfileRequest

class NewProfile(
    var login: String? = null,
    var name: String? = null,
    var description: String? = null,
    var age: String? = null,
    var city: City? = null
) {
    constructor(profile: Profile) : this(
        profile.login,
        profile.name,
        profile.description,
        profile.age.toString(),
        City(profile.cityId, profile.cityName)
    )

    fun toRequest(): ProfileRequest {
        return ProfileRequest(login, name, description, age?.toInt(), city?.cityId)
    }
}