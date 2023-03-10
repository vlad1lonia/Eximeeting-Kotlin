package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


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

        val deviceName: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
        Log.i(TAG, "Device name: $deviceName")

        firebaseAuth = FirebaseAuth.getInstance()
        binding.registerCompletedButton.setOnClickListener { registerUser() }

        setContentView(binding.root)
    }

    private fun registerUser() {
        fullName = binding.fullNameEdittext.text.toString().trim()
        email = binding.emailEdittext.text.toString().trim()
        companyName = binding.companyNameEdittext.text.toString().trim()
        password = binding.passwordEdittext.text.toString().trim()

        checkCredentials()

        createAccount()
    }

    private fun createAccount() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            run {
                if (task.isSuccessful) {
                    val user = User(fullName, email, companyName)
                    FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it1.uid).setValue(user).addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(this@RegisterActivity,
                                        "Account is created successfully!",
                                        Toast.LENGTH_SHORT).show()
                                    Log.i(TAG, "Creating ActivityMain")
                                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
//                                    packageManager.setComponentEnabledSetting()
                                } else {
                                    Toast.makeText(this@RegisterActivity,
                                        "Failed to create an account! Try again",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@RegisterActivity,
                        "Failed to create an account", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkCredentials() {
        if (fullName.isEmpty()) {
            binding.fullNameEdittext.error = "Full name is required!"
            binding.fullNameEdittext.requestFocus()
            return
        } else {
            binding.fullNameEdittext.error = null
            binding.fullNameEdittext.requestFocus()
        }

        if (email.isEmpty()) {
            binding.emailEdittext.error = "Email is required!"
            binding.emailEdittext.requestFocus()
            return
        } else {
            binding.emailEdittext.error = null
            binding.emailEdittext.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEdittext.error = "Please provide valid email!"
            binding.emailEdittext.requestFocus()
            return
        } else {
            binding.emailEdittext.error = null
            binding.emailEdittext.requestFocus()
        }

        if (companyName.isEmpty()) {
            binding.companyNameEdittext.error = "Company name is required!"
            binding.companyNameEdittext.requestFocus()
            return
        } else {
            binding.companyNameEdittext.error = null
            binding.companyNameEdittext.requestFocus()
        }

        if (password.isEmpty()) {
            binding.passwordEdittext.error = "Password is required!"
            binding.passwordEdittext.requestFocus()
            return
        } else {
            binding.passwordEdittext.error = null
            binding.passwordEdittext.requestFocus()
        }

        if (password.length < 6) {
            binding.passwordEdittext.error = "Password should contain at least 6 characters"
            binding.passwordEdittext.requestFocus()
            return
        } else {
            binding.passwordEdittext.error = null
            binding.passwordEdittext.requestFocus()
        }
    }
}