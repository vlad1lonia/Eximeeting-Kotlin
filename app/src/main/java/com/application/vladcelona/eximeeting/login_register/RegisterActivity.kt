package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.application.vladcelona.eximeeting.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "RegisterActivity"

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var companyName: String
    private lateinit var password: String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val deviceName: String = Settings.Secure
            .getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.i(TAG, "Device name: $deviceName")

        firebaseAuth = FirebaseAuth.getInstance()
        binding.registerCompletedButton.setOnClickListener { registerUser() }

        setContentView(binding.root)
    }

    private fun registerUser() {

    }
}