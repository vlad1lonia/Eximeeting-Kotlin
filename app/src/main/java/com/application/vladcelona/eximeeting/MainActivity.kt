package com.application.vladcelona.eximeeting

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()

        EximeetingFirebase.testFirestore()
        MapKitInitializer.initialize("eb50f8b7-3004-48a1-aff6-da437619b1b8", this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = this.findNavController(R.id.nav_host_fragment)

        // Find reference to bottom navigation view
        navView = findViewById(R.id.bottom_nav_view)

        // Hook navigation controller to bottom navigation view
        navView.setupWithNavController(navController)
        navView.visibility = View.INVISIBLE

        val navOptions = NavOptions.Builder().setPopUpTo(R.id.startFragment, true).build()
        navController.navigate(R.id.startFragment, bundleOf(), navOptions)
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }

    object MapKitInitializer {

        private var initialized = false

        fun initialize(apiKey: String, context: Context) {
            if (initialized) {
                return
            }

            MapKitFactory.setApiKey(apiKey)
            MapKitFactory.initialize(context)
            initialized = true
        }
    }
}