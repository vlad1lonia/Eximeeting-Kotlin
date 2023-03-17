package com.application.vladcelona.eximeeting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.application.vladcelona.eximeeting.databinding.ActivityMainBinding
import com.application.vladcelona.eximeeting.login_register.PickActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Users")

        setContent()
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Activity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "Activity onRestart")
    }

    private fun setContent() {
        Log.i(TAG, "$TAG created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainActivityText.visibility = View.INVISIBLE

        val startFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (startFragment == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,
                UpcomingFragment.newInstance()).commit()
        }

        binding.bottomNavigationBar.background = ContextCompat
            .getDrawable(this@MainActivity, R.color.personal_green_background)

        val badge = binding.bottomNavigationBar.getOrCreateBadge(R.id.home)
        badge.isVisible = true

        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }

        setContentView(binding.root)

//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if (currentFragment == null) {
//            val fragment = EventListFragment.newInstance()
//            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
//        }
    }

//    override fun onStop() {
//        super.onStop()
//        Log.i(TAG, "Activity onStop")
//        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.i(TAG, "Activity onDestroy")
//        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
//                Toast.makeText(this@MainActivity,
//                    "You clicked home icon!", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    UpcomingFragment.newInstance()).commit()
//                binding.bottomNavigationBar.getOrCreateBadge(item.itemId).number++
                return true
            }
            R.id.ended -> {
//                Toast.makeText(this@MainActivity,
//                    "You clicked completed icon!", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    CompletedFragment.newInstance()).commit()
//                binding.bottomNavigationBar.getOrCreateBadge(item.itemId).number++
                return true
            }
            R.id.settings -> {
//                Toast.makeText(this@MainActivity,
//                    "You clicked settings icon!", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    SettingsFragment.newInstance()).commit()
//                binding.bottomNavigationBar.getOrCreateBadge(item.itemId).number++
                return true
            }
            else -> return false
        }
    }
}