package com.singlelab.lume.model.profile

import com.singlelab.lume.model.city.City
import com.singlelab.net.model.person.UpdateProfileRequest

class NewProfile(
    var name: String? = null,
    var description: String? = null,
    var age: Int? = null,
    var city: City? = null
) {
    constructor(profile: Profile) : this(
        profile.name,
        profile.description,
        profile.age,
        City(profile.cityId, profile.cityName)
    )

    fun toRequest(): UpdateProfileRequest {
        return UpdateProfileRequest(name, description, age, city?.cityId)
    }
}