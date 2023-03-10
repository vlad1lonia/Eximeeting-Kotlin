package com.application.vladcelona.eximeeting

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.application.vladcelona.eximeeting.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainActivityText.text = Build.ID

        setContentView(binding.root)
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
}