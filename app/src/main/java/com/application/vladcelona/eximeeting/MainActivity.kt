package com.application.vladcelona.eximeeting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

//    lateinit var mainBinding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        navController = this.findNavController(R.id.nav_host_fragment)

        // Find reference to bottom navigation view
        navView = findViewById(R.id.bottom_nav_view)

        // Hook your navigation controller to bottom navigation view
        navView.setupWithNavController(navController)
        navView.visibility = View.INVISIBLE

        val navOptions = NavOptions.Builder().setPopUpTo(R.id.startFragment, true).build()

        if (User.checkAccess()) {
            Toast.makeText(this@MainActivity,
                "You have been logged in successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_startFragment_to_upcomingEventsListFragment,
                bundleOf(), navOptions)
            navView.visibility = View.VISIBLE
        }
    }
}