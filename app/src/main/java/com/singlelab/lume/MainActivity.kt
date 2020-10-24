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
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.singlelab.lume.analytics.Analytics
import com.singlelab.lume.base.listeners.OnActivityResultListener
import com.singlelab.lume.base.listeners.OnBackPressListener
import com.singlelab.lume.base.listeners.OnPermissionListener
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.model.target.Target
import com.singlelab.lume.model.target.TargetType
import com.singlelab.lume.util.parseDeepLink
import com.yandex.metrica.YandexMetrica
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
        Analytics.init()
        getPushToken()
        val data: Uri? = intent?.data
        var target: Target? = null
        if (data == null) {
            if (intent.extras != null && intent.extras!!.containsKey(Const.URL_KEY)) {
                val url = intent?.extras?.get(Const.URL_KEY).toString()
                if (url.contains(Const.GOOGLE_PLAY)) {
                    openBrowser(url)
                } else {
                    target = url.parseDeepLink()
                }
            }
        } else {
            target = data.toString().parseDeepLink()
        }
        target?.let {
            toTarget(navController, it)
        }
        getDynamicLink(navController)
        fetchFacebookLinks()
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun fetchFacebookLinks() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(
            this
        ) {
            YandexMetrica.reportReferralUrl(it?.targetUri.toString());
        }
        //если после установки через рекламу запустили приложение не сразу
        AppLinkData.createFromActivity(this)?.let {
            YandexMetrica.reportAppOpen(it.targetUri.toString())
        }
    }

    private fun getDynamicLink(navController: NavController) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                val deepLink: Uri?
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    deepLink.toString().parseDeepLink()?.let {
                        toTarget(navController, it)
                    }
                }
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(
                    this,
                    "getDynamicLink:onFailure ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
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
        loading_text?.visibility = View.GONE
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

    fun checkWriteExternalPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    permissionListener?.onWriteExternalPermissionGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    permissionListener?.onWriteExternalPermissionDenied()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    permissionListener?.onWriteExternalPermissionDenied()
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
        badgeProfile.isVisible = notifications.hasNewFriends || notifications.hasNewBadges
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

    fun showLoadingText(text: String) {
        showLoading(true)
        loading_text.text = text
        loading_text.visibility = View.VISIBLE
    }
}