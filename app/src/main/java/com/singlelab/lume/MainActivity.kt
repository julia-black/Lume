package com.singlelab.lume

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_my_profile
//            )
//        )
        //  setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        super.onTitleChanged(title, color)
        toolbar.title = title ?: ""
    }

    fun showLoading(isShow: Boolean) {
        loading.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}