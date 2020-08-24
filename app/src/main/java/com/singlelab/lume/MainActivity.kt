package com.singlelab.lume

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
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
import com.singlelab.lume.model.profile.PersonNotifications
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
        logOnCreate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activityResultListener != null) {
            activityResultListener?.onActivityResultFragment(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (backPressListener != null) {
            backPressListener!!.onBackPressed()
        } else {
            super.onBackPressed()
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

    fun checkContactsPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    permissionListener?.onContactsPermissionGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    permissionListener?.onContactsPermissionDenied()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    permissionListener?.onContactsPermissionDenied()
                }
            }).check()
    }

    fun showBottomNavigation(isShow: Boolean) {
        nav_view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun showNotifications(notifications: PersonNotifications) {
        val badgeEvents = nav_view.getOrCreateBadge(R.id.events)
        badgeEvents.backgroundColor = ContextCompat.getColor(this, R.color.colorNotification)
        badgeEvents.isVisible = notifications.hasNewEvents

        val badgeChats = nav_view.getOrCreateBadge(R.id.chats)
        badgeChats.backgroundColor = ContextCompat.getColor(this, R.color.colorNotification)
        badgeChats.isVisible = notifications.hasNewChatMessages

        val badgeProfile = nav_view.getOrCreateBadge(R.id.my_profile)
        badgeProfile.backgroundColor = ContextCompat.getColor(this, R.color.colorNotification)
        badgeProfile.isVisible = notifications.hasNewFriends
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

    private fun logOnCreate() {
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "test")
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
//        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//        val options = FirebaseOptions.Builder()
//            .setApplicationId("1:852373751034:android:e4379c99c5c88e3c36b543") // Required for Analytics.
//            .setProjectId("lume-285006") // Required for Firebase Installations.
//            .setApiKey("AIzaSyCGvIvjW2n-H_x8QPOnjTxQLEwgDlLT5Gw") // Required for Auth.
//            .build()
//        FirebaseApp.initializeApp(this, options, "Lume")
    }
}