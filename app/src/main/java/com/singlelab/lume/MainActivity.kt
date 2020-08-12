package com.singlelab.lume

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnBackPressListener
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.target.Target
import com.singlelab.lume.model.target.TargetType
import com.singlelab.lume.util.parseDeepLink
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
        getPushToken()
        val data: Uri? = intent?.data
        val target = data?.toString()?.parseDeepLink()
        target?.let {
            toTarget(navController, it)
        }
    }

    fun showLoading(isShow: Boolean, withoutBackground: Boolean = false) {
        if (withoutBackground) {
            loading_without_back.visibility = if (isShow) View.VISIBLE else View.GONE
            loading.visibility = View.GONE
        } else {
            loading_without_back.visibility = View.GONE
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

    private fun getPushToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result?.token ?: return@OnCompleteListener
                LumeApplication.preferences?.setPushToken(token)
            })
    }

    private fun toTarget(navController: NavController, target: Target) {
        when (target.target) {
            TargetType.EVENT -> {
                navController.navigate(R.id.event, bundleOf("eventUid" to target.targetId))
            }
        }
    }
}