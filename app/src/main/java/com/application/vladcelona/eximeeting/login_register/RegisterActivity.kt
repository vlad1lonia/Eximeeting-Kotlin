package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var birthDate: String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "$TAG created")
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val deviceName: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
        Log.i(TAG, "Device name: $deviceName")

        firebaseAuth = FirebaseAuth.getInstance()

        binding.birthDateEdittext.addTextChangedListener(object : TextWatcher {

            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var cleanString = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cleanLength = cleanString.length
                    var selection = cleanLength

                    var index = 2
                    while (index <= cleanLength && index < 6) {
                        selection++
                        index += 2
                    }

                    // Fix for pressing delete next to a forward slash
                    if (cleanString == cleanC) selection--
                    if (cleanString.length < 8) {
                        cleanString = cleanString + ddmmyyyy.substring(cleanString.length)
                    } else {
                        // This part makes sure that when we finish entering numbers
                        // The date is correct, fixing it otherwise
                        var day = cleanString.substring(0, 2).toInt()
                        var mon = cleanString.substring(2, 4).toInt()
                        var year = cleanString.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal[Calendar.MONTH] = mon - 1
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal[Calendar.YEAR] = year
                        // ^ first set year for the line below to work correctly
                        // With leap years - otherwise, date e.g. 29/02/2012
                        // Would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        cleanString = String.format("%02d%02d%02d", day, mon, year)
                    }

                    cleanString = String.format(
                        "%s/%s/%s", cleanString.substring(0, 2),
                        cleanString.substring(2, 4), cleanString.substring(4, 8)
                    )

                    selection = if (selection < 0) 0 else selection
                    current = cleanString

                    binding.birthDateEdittext.setText(current)
                    binding.birthDateEdittext.setSelection(if (selection < current.length)
                        selection else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            
            override fun afterTextChanged(s: Editable) {}
        })
        
        binding.registerCompletedButton.setOnClickListener { registerUser() }

        setContentView(binding.root)
    }

    private fun registerUser() {
        fullName = binding.fullNameEdittext.text.toString().trim()
        companyName = binding.companyNameEdittext.text.toString().trim()
        email = binding.emailEdittext.text.toString().trim()
        birthDate = binding.birthDateEdittext.text.toString().trim()
        password = binding.passwordEdittext.text.toString().trim()

        checkCredentials()
    }

    private fun createAccount() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            run {
                if (task.isSuccessful) {
                    val user = User(fullName, email, companyName, birthDate)
                    FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it1.uid).setValue(user).addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(this@RegisterActivity,
                                        "Account is created successfully!",
                                        Toast.LENGTH_SHORT).show()
                                    Log.i(TAG, "Creating ActivityMain")
                                    startActivity(Intent(this@RegisterActivity,
                                        MainActivity::class.java))
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

        if (companyName.isEmpty()) {
            binding.companyNameEdittext.error = "Company name is required!"
            binding.companyNameEdittext.requestFocus()
            return
        } else {
            binding.companyNameEdittext.error = null
            binding.companyNameEdittext.requestFocus()
        }

        if (birthDate.isEmpty()) {
            binding.birthDateEdittext.error = "birthDate is required!"
            binding.birthDateEdittext.requestFocus()
            return
        } else {
            binding.birthDateEdittext.error = null
            binding.birthDateEdittext.requestFocus()
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

        createAccount()
    }
}