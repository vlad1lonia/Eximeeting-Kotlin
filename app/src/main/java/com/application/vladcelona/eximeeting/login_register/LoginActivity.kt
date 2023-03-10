package com.application.vladcelona.eximeeting.login_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityLoginBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginCompletedButton.setOnClickListener { userLogin() }
        setContentView(binding.root)
    }

    private fun userLogin() {
        email = binding.emailEdittext.text!!.toString().trim()
        password = binding.passwordEdittext.text!!.toString().trim()

        checkCredentials()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Starting new Intent: MainActivity")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this@LoginActivity,
                        "Failed to login! Please check your credentials!",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkCredentials() {
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