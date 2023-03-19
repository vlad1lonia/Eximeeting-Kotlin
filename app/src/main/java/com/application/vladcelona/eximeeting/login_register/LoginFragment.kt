package com.application.vladcelona.eximeeting.login_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.application.vladcelona.eximeeting.MainActivity
import com.application.vladcelona.eximeeting.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var loginCompletedButton: Button
    
    private lateinit var emailEdittext: EditText
    private lateinit var passwordEdittext: EditText

    private lateinit var email: String
    private lateinit var password: String

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        
        emailEdittext = view.findViewById(R.id.email_edittext)
        passwordEdittext = view.findViewById(R.id.password_edittext)

        loginCompletedButton = view.findViewById(R.id.login_completed_button) as Button
        loginCompletedButton.setOnClickListener { userLogin() }

        return view
    }

    private fun userLogin() {
        email = emailEdittext.text!!.toString().trim()
        password = passwordEdittext.text!!.toString().trim()

        checkCredentials()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                Log.i(TAG, "Starting new Intent: MainActivity")
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                Toast.makeText(context,
                    "Failed to login! Please check your credentials!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCredentials() {
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
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}