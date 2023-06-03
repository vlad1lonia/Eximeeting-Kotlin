package com.application.vladcelona.eximeeting.account_access

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.user.User
import com.application.vladcelona.eximeeting.databinding.FragmentRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RegisterFragment"

// TODO: Change Realtime Database for Firestore
class RegisterFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var fullName: String
    private lateinit var email: String
    private lateinit var companyName: String
    private lateinit var password: String
    private lateinit var birthDate: String
    private lateinit var profileImageString: String

    private lateinit var profileImage: Bitmap

    private lateinit var defaultMap: HashMap<String, Boolean>

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.birthDateEditText.addTextChangedListener(object : TextWatcher {

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

                    binding.birthDateEditText.setText(currentString)
                    binding.birthDateEditText.setSelection(if (selection < currentString.length)
                        selection else currentString.length)
                }
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(sequence: Editable) {}
        })

        binding.registerCompletedButton.setOnClickListener { it.hideKeyboard(); registerUser() }

        return binding.root
    }

    private fun registerUser() {
        fullName = binding.fullNameEditText.text.toString().trim()
        companyName = binding.companyNameEditText.text.toString().trim()
        email = binding.emailEditText.text.toString().trim()
        birthDate = binding.birthDateEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()

        profileImageString = bitmapToString()
        checkCredentials()
    }

    private fun checkCredentials() {
        if (fullName.isEmpty()) {
            binding.fullNameEditText.error = "Full name is required!"
            binding.fullNameEditText.requestFocus()
            return
        } else {
            binding.fullNameEditText.error = null
            binding.fullNameEditText.requestFocus()
        }

        if (companyName.isEmpty()) {
            binding.companyNameEditText.error = "Company name is required!"
            binding.companyNameEditText.requestFocus()
            return
        } else {
            binding.companyNameEditText.error = null
            binding.companyNameEditText.requestFocus()
        }

        if (birthDate.isEmpty()) {
            binding.birthDateEditText.error = "birthDate is required!"
            binding.birthDateEditText.requestFocus()
            return
        } else {
            binding.birthDateEditText.error = null
            binding.birthDateEditText.requestFocus()
        }

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required!"
            binding.emailEditText.requestFocus()
            return
        } else {
            binding.emailEditText.error = null
            binding.emailEditText.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Provide valid email!"
            binding.emailEditText.requestFocus()
            return
        } else {
            binding.emailEditText.error = null
            binding.emailEditText.requestFocus()
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required!"
            binding.passwordEditText.requestFocus()
            return
        } else {
            binding.passwordEditText.error = null
            binding.passwordEditText.requestFocus()
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password should contain at least 6 characters"
            binding.passwordEditText.requestFocus()
            return
        } else {
            binding.passwordEditText.error = null
            binding.passwordEditText.requestFocus()
        }

        createAccount()
    }

    // TODO: Change Realtime Database for Firestore
    private fun createAccount() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {

                    val user = User(
                        fullName = fullName,
                        email = email,
                        companyName = companyName,
                        birthDate = birthDate,
                        profileImage = profileImageString,
                        visitedEvents = readJsonFromFile()
                    )

                    // TODO: Change Realtime Database for Firestore
                    FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it1.uid).setValue(user).addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(context,
                                        "Account is created successfully!",
                                        Toast.LENGTH_SHORT).show()
                                    Log.i(TAG, "Creating ActivityMain")

                                    val navView = activity?.
                                    findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                                    navView?.visibility = View.VISIBLE
                                    findNavController()
                                        .navigate(R.id.action_startFragment_to_upcomingEventsListFragment)
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

    /**
     * A method for reading a json file with default information about the events visited by user
     * This file is used only once when user creates an account
     * @return A new instance of String
     */
    fun readJsonFromFile(): String {
        val inputStream: InputStream = resources.openRawResource(R.raw.events_visit_status)
        val writer: Writer = StringWriter()
        val bufferArray: CharArray = CharArray(1024)

        inputStream.use { inputStream ->
            val reader: Reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var status: Int = reader.read(bufferArray)

            while (status != -1) {
                writer.write(bufferArray, 0, status)
                status = reader.read(bufferArray)
            }
        }

        return writer.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}