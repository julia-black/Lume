package com.singlelab.lume.ui.auth.router

import androidx.navigation.NavController
import com.singlelab.lume.R

class AuthRouterImpl : AuthRouter {
    override fun navigateToMyProfile(navController: NavController) {
        navController.popBackStack()
        navController.navigate(R.id.navigation_my_profile)
    }
}