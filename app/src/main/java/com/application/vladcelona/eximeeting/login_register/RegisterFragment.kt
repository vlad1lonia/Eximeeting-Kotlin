package com.application.vladcelona.eximeeting.login_register

import android.annotation.SuppressLint
import android.content.Context
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
import android.view.inputmethod.InputMethodManager
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

    private lateinit var fullNameEditText: EditText
    private lateinit var companyNameEditText: EditText
    private lateinit var birthDateEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var registerCompletedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        firebaseAuth = FirebaseAuth.getInstance()
        profileImage = context?.let {
            ContextCompat.getDrawable(it, R.drawable.person_icon)?.toBitmap() }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        fullNameEditText = view.findViewById(R.id.full_name_edittext) as EditText
        companyNameEditText = view.findViewById(R.id.company_name_edittext) as EditText
        birthDateEditText = view.findViewById(R.id.birth_date_edittext) as EditText
        emailEditText = view.findViewById(R.id.email_edittext) as EditText
        passwordEditText = view.findViewById(R.id.password_edittext) as EditText

        registerCompletedButton = view.findViewById(R.id.register_completed_button) as Button

        birthDateEditText.addTextChangedListener(object : TextWatcher {

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
                        day = if (day > calendar.getActualMaximum(Calendar.DATE))
                            calendar.getActualMaximum(Calendar.DATE) else day
                        cleanString = String.format("%02d%02d%02d", day, month, year)
                    }

                    cleanString = String.format(
                        "%s/%s/%s", cleanString.substring(0, 2),
                        cleanString.substring(2, 4), cleanString.substring(4, 8)
                    )

                    selection = if (selection < 0) 0 else selection
                    currentString = cleanString

                    birthDateEditText.setText(currentString)
                    birthDateEditText.setSelection(if (selection < currentString.length)
                        selection else currentString.length)
                }
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(sequence: Editable) {}
        })

        registerCompletedButton.setOnClickListener { it.hideKeyboard(); registerUser() }

        return view
    }

    private fun registerUser() {
        fullName = fullNameEditText.text.toString().trim()
        companyName = companyNameEditText.text.toString().trim()
        email = emailEditText.text.toString().trim()
        birthDate = birthDateEditText.text.toString().trim()
        password = passwordEditText.text.toString().trim()

        profileImageString = bitmapToString()
        checkCredentials()
    }

    private fun checkCredentials() {
        if (fullName.isEmpty()) {
            fullNameEditText.error = "Full name is required!"
            fullNameEditText.requestFocus()
            return
        } else {
            fullNameEditText.error = null
            fullNameEditText.requestFocus()
        }

        if (companyName.isEmpty()) {
            companyNameEditText.error = "Company name is required!"
            companyNameEditText.requestFocus()
            return
        } else {
            companyNameEditText.error = null
            companyNameEditText.requestFocus()
        }

        if (birthDate.isEmpty()) {
            birthDateEditText.error = "birthDate is required!"
            birthDateEditText.requestFocus()
            return
        } else {
            birthDateEditText.error = null
            birthDateEditText.requestFocus()
        }

        if (email.isEmpty()) {
            emailEditText.error = "Email is required!"
            emailEditText.requestFocus()
            return
        } else {
            emailEditText.error = null
            emailEditText.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please provide valid email!"
            emailEditText.requestFocus()
            return
        } else {
            emailEditText.error = null
            emailEditText.requestFocus()
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required!"
            passwordEditText.requestFocus()
            return
        } else {
            passwordEditText.error = null
            passwordEditText.requestFocus()
        }

        if (password.length < 6) {
            passwordEditText.error = "Password should contain at least 6 characters"
            passwordEditText.requestFocus()
            return
        } else {
            passwordEditText.error = null
            passwordEditText.requestFocus()
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

    @SuppressLint("ServiceCast")
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}