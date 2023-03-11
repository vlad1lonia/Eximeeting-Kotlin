package com.application.vladcelona.eximeeting

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity;
import com.application.vladcelona.eximeeting.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainActivityText.text = Build.ID

        val badge = binding.bottomNavigationBar.getOrCreateBadge(R.id.home)
        badge.isVisible = true; badge.number = 1

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

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Activity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "Activity onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "Activity onStop")
        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Activity onDestroy")
        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {



        when (item.itemId) {
            R.id.home -> {
                Toast.makeText(this@MainActivity,
                    "You clicked home icon!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.ended -> {
                Toast.makeText(this@MainActivity,
                    "You clicked completed icon!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.settings -> {
                Toast.makeText(this@MainActivity,
                    "You clicked settings icon!", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return false
        }
    }
}