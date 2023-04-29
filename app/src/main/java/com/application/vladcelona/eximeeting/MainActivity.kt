package com.application.vladcelona.eximeeting

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.vladcelona.eximeeting.completed.CompletedListFragment
import com.application.vladcelona.eximeeting.databinding.ActivityMainBinding
import com.application.vladcelona.eximeeting.settings.SettingsFragment
import com.application.vladcelona.eximeeting.upcoming.UpcomingListFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.getReference("Users")

        setContent()
    }

    private fun setContent() {
        Log.i(TAG, "$TAG created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainActivityText.visibility = View.INVISIBLE

        val startFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (startFragment == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,
                UpcomingListFragment.newInstance()).commit()
        }

        binding.bottomNavigationBar.background = ContextCompat
            .getDrawable(this@MainActivity, R.color.personal_green_background)

        val badge = binding.bottomNavigationBar.getOrCreateBadge(R.id.home)
        badge.isVisible = true

        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }

        setContentView(binding.root)
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
                onItemPressed(
                    UpcomingListFragment.newInstance(),
                    CompletedListFragment.newInstance(), SettingsFragment.newInstance())
                return true
            }
            R.id.ended -> {
                onItemPressed(
                    CompletedListFragment.newInstance(),
                    UpcomingListFragment.newInstance(), SettingsFragment.newInstance())
                return true
            }
            R.id.settings -> {
                onItemPressed(SettingsFragment.newInstance(),
                    UpcomingListFragment.newInstance(), CompletedListFragment.newInstance())
                return true
            }
            else -> return false
        }
    }

    private fun onItemPressed(chosenFragment: Fragment, hideFirstFragment: Fragment,
                              hideSecondFragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(hideFirstFragment).hide(hideSecondFragment)
            .add(R.id.fragment_container, chosenFragment, null).addToBackStack(null).commit()
    }
}