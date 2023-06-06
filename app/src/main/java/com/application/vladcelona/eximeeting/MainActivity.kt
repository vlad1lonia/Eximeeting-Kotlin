package com.application.vladcelona.eximeeting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.jsonToMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.yandex.mapkit.MapKitFactory


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()

        EximeetingFirebase.testPushFirestore()
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

    fun testFunction() {

        val hashMap: HashMap<String, Int> = HashMap()
        hashMap["a"] = 1
        hashMap["b"] = 2
        hashMap["c"] = 3

        val convertedHashMap: String = Gson().toJson(hashMap)
        Log.i(TAG, hashMap.toString())
        Log.i(TAG, convertedHashMap)

        val reconvertedHashMap: HashMap<String, Int> =
            jsonToMap(convertedHashMap) as HashMap<String, Int>
        Log.i(TAG, reconvertedHashMap.toString())
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