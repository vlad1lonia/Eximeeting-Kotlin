package com.application.vladcelona.eximeeting.login_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "StartActivity"

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityStartBinding.inflate(layoutInflater)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Users")

        // Check if user has been logged in before
        FirebaseAuth.getInstance().currentUser?.let { databaseReference.child(it.uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    Toast.makeText(this, "Successful Login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
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
            Log.i(TAG, "Starting new Intent: LoginActivity")
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            Log.i(TAG, "Starting new Intent: RegisterActivity")
            startActivity(Intent(this@StartActivity, RegisterActivity::class.java))
        }

        setContentView(binding.root)
    }
}