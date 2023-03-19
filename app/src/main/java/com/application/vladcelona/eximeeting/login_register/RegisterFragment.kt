package com.application.vladcelona.eximeeting.login_register

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var companyName: String
    private lateinit var password: String
    private lateinit var birthDate: String
    private lateinit var profileImageString: String

    private lateinit var profileImage: Bitmap

    private lateinit var fullNameEdittext: EditText
    private lateinit var companyNameEdittext: EditText
    private lateinit var birthDateEdittext: EditText
    private lateinit var emailEdittext: EditText
    private lateinit var passwordEdittext: EditText

    private lateinit var registerCompletedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        fullNameEdittext = view.findViewById(R.id.full_name_edittext) as EditText
        companyNameEdittext = view.findViewById(R.id.company_name_edittext) as EditText
        birthDateEdittext = view.findViewById(R.id.birth_date_edittext) as EditText
        emailEdittext = view.findViewById(R.id.email_edittext) as EditText
        passwordEdittext = view.findViewById(R.id.password_edittext) as EditText

        profileImage = context?.let {
            ContextCompat.getDrawable(it, R.drawable.completed_icon)?.toBitmap() }!!

        registerCompletedButton = view.findViewById(R.id.register_completed_button) as Button

        birthDateEdittext.addTextChangedListener(object : TextWatcher {

            private var currentString = ""
            private val dateFormat = "DDMMYYYY"
            private val calendar = Calendar.getInstance()

            override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
                if (sequence.toString() != currentString) {
                    var cleanString = sequence.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = currentString.replace("[^\\d.]".toRegex(), "")
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
                        cleanString += dateFormat.substring(cleanString.length)
                    } else {
                        // This part makes sure that when we finish entering numbers
                        // The date is correct, fixing it otherwise
                        var day = cleanString.substring(0, 2).toInt()
                        var month = cleanString.substring(2, 4).toInt()
                        var year = cleanString.substring(4, 8).toInt()
                        if (month > 12) month = 12
                        calendar[Calendar.MONTH] = month - 1
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        calendar[Calendar.YEAR] = year
                        // ^ first set year for the line below to work correctly
                        // With leap years - otherwise, date e.g. 29/02/2012
                        // Would be automatically corrected to 28/02/2012
                        day = if (day > calendar.getActualMaximum(Calendar.DATE)) calendar.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        cleanString = String.format("%02d%02d%02d", day, month, year)
                    }

                    cleanString = String.format(
                        "%s/%s/%s", cleanString.substring(0, 2),
                        cleanString.substring(2, 4), cleanString.substring(4, 8)
                    )

                    selection = if (selection < 0) 0 else selection
                    currentString = cleanString

                    birthDateEdittext.setText(currentString)
                    birthDateEdittext.setSelection(if (selection < currentString.length)
                        selection else currentString.length)
                }
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(sequence: Editable) {}
        })

        registerCompletedButton.setOnClickListener { registerUser() }

        return view
    }

    private fun registerUser() {
        fullName = fullNameEdittext.text.toString().trim()
        companyName = companyNameEdittext.text.toString().trim()
        email = emailEdittext.text.toString().trim()
        birthDate = birthDateEdittext.text.toString().trim()
        password = passwordEdittext.text.toString().trim()

        profileImageString = bitmapToString()
        checkCredentials()
    }

    private fun checkCredentials() {
        if (fullName.isEmpty()) {
            fullNameEdittext.error = "Full name is required!"
            fullNameEdittext.requestFocus()
            return
        } else {
            fullNameEdittext.error = null
            fullNameEdittext.requestFocus()
        }

        if (companyName.isEmpty()) {
            companyNameEdittext.error = "Company name is required!"
            companyNameEdittext.requestFocus()
            return
        } else {
            companyNameEdittext.error = null
            companyNameEdittext.requestFocus()
        }

        if (birthDate.isEmpty()) {
            birthDateEdittext.error = "birthDate is required!"
            birthDateEdittext.requestFocus()
            return
        } else {
            birthDateEdittext.error = null
            birthDateEdittext.requestFocus()
        }

        if (email.isEmpty()) {
            emailEdittext.error = "Email is required!"
            emailEdittext.requestFocus()
            return
        } else {
            emailEdittext.error = null
            emailEdittext.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdittext.error = "Please provide valid email!"
            emailEdittext.requestFocus()
            return
        } else {
            emailEdittext.error = null
            emailEdittext.requestFocus()
        }

        if (password.isEmpty()) {
            passwordEdittext.error = "Password is required!"
            passwordEdittext.requestFocus()
            return
        } else {
            passwordEdittext.error = null
            passwordEdittext.requestFocus()
        }

        if (password.length < 6) {
            passwordEdittext.error = "Password should contain at least 6 characters"
            passwordEdittext.requestFocus()
            return
        } else {
            passwordEdittext.error = null
            passwordEdittext.requestFocus()
        }

        createAccount()
    }

    private fun createAccount() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {
                    val user = User(fullName, email, companyName, birthDate, profileImageString)
                    FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it1.uid).setValue(user).addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(context,
                                        "Account is created successfully!",
                                        Toast.LENGTH_SHORT).show()
                                    Log.i(TAG, "Creating ActivityMain")
                                    startActivity(
                                        Intent(context,
                                        MainActivity::class.java)
                                    )
//                                    packageManager.setComponentEnabledSetting()
                                } else {
                                    Toast.makeText(context,
                                        "Failed to create an account! Try again",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    }
                } else {
                    Toast.makeText(context,
                        "Failed to create an account", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bitmapToString(): String {
        val imageArray = ByteArrayOutputStream()
        profileImage.compress(Bitmap.CompressFormat.PNG, 100, imageArray)
        val converted = imageArray.toByteArray()
        return Base64.getEncoder().encodeToString(converted)
    }

    companion object {
        @JvmStatic
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}