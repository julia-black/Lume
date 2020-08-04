package com.singlelab.lume

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnBackPressListener
import com.singlelab.lume.base.listeners.OnPermissionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var activityResultListener: OnActivityResultListener? = null

    private var permissionListener: OnPermissionListener? = null

    private var backPressListener: OnBackPressListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    fun showLoading(isShow: Boolean, withoutBackground: Boolean = false) {
        if (withoutBackground) {
            loading_without_back.visibility = if (isShow) View.VISIBLE else View.GONE
        } else {
            loading.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    fun setActivityListener(listener: OnActivityResultListener) {
        this.activityResultListener = listener
    }

    fun setPermissionListener(listener: OnPermissionListener) {
        this.permissionListener = listener
    }

    fun setBackPressListener(listener: OnBackPressListener?) {
        this.backPressListener = listener
    }

    fun checkLocationPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    permissionListener?.onLocationPermissionGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    permissionListener?.onLocationPermissionDenied()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    permissionListener?.onLocationPermissionDenied()
                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activityResultListener != null) {
            activityResultListener?.onActivityResultFragment(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showBottomNavigation(isShow: Boolean) {
        nav_view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        if (backPressListener != null) {
            backPressListener!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}