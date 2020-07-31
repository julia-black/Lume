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
import com.singlelab.lume.base.listeners.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var logoutListener: OnLogoutListener? = null

    private var searchListener: OnSearchListener? = null

    private var filterListener: OnFilterListener? = null

    private var activityResultListener: OnActivityResultListener? = null

    private var permissionListener: OnPermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_events, R.id.navigation_my_profile
//            )
//        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    logoutListener?.onClickLogout()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_search -> {
                    searchListener?.onClickSearch()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_filter -> {
                    filterListener?.onClickFilter()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        super.onTitleChanged(title, color)
        toolbar.title = title ?: ""
    }

    fun showLoading(isShow: Boolean) {
        loading.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setLogoutListener(listener: OnLogoutListener) {
        this.logoutListener = listener
    }

    fun setSearchListener(listener: OnSearchListener) {
        this.searchListener = listener
    }

    fun setActivityListener(listener: OnActivityResultListener) {
        this.activityResultListener = listener
    }

    fun setFilterListener(listener: OnFilterListener) {
        this.filterListener = listener
    }

    fun setPermissionListener(listener: OnPermissionListener) {
        this.permissionListener = listener
    }

    fun showLogoutInToolbar(isShow: Boolean) {
        toolbar.menu.findItem(R.id.menu_logout).isVisible = isShow
    }

    fun showSearchInToolbar(isShow: Boolean) {
        toolbar.menu.findItem(R.id.menu_search).isVisible = isShow
    }

    fun showFilterInToolbar(isShow: Boolean) {
        toolbar.menu.findItem(R.id.menu_filter).isVisible = isShow
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
}