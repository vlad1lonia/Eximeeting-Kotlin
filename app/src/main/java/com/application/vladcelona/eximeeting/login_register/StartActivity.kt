package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.ActivityStartBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.util.*

private const val TAG = "StartActivity"

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "StartActivity created")
        binding = ActivityStartBinding.inflate(layoutInflater)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Users")

        // Check if user has an account in firebase
        FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
                databaseReference.child(it1.uid).get().addOnCompleteListener {
                    task: Task<DataSnapshot> ->
                    if (task.isSuccessful && task.result.exists()) {
                        Toast.makeText(this@StartActivity,
                            "You have been logged in successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    }
                }
            }
        }

        // Set up animation for StartActivity Views
        val appNameAnimation = AnimationUtils.loadAnimation(
            this@StartActivity, R.anim.app_name_animation)
        val descriptionAnimation = AnimationUtils.loadAnimation(
            this@StartActivity, R.anim.description_animation)
        val buttonsAnimation = AnimationUtils.loadAnimation(
            this@StartActivity, R.anim.buttons_animation)

        // Apply animations to Views
        binding.startScreenAppName.startAnimation(appNameAnimation)
        binding.startScreenDescription.startAnimation(descriptionAnimation)
        binding.loginButton.startAnimation(buttonsAnimation)
        binding.registerButton.startAnimation(buttonsAnimation)

        binding.loginButton.setOnClickListener {
            Log.i(TAG, "Creating LoginActivity")
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            Log.i(TAG, "Creating RegisterActivity")
            startActivity(Intent(this@StartActivity, RegisterActivity::class.java))
        }

        setContentView(binding.root)
    }
}