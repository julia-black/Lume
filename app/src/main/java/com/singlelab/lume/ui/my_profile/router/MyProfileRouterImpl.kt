package com.singlelab.lume.ui.my_profile.router

import androidx.navigation.NavController
import com.singlelab.lume.R

class MyProfileRouterImpl : MyProfileRouter {

    override fun navigateToAuth(navController: NavController) {
        navController.popBackStack()
        navController.navigate(R.id.navigation_auth)
    }
}