package com.application.vladcelona.eximeeting.account_access

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginCompletedButton.setOnClickListener { it.hideKeyboard(); userLogin() }

        return binding.root
    }

    private fun userLogin() {
        email = binding.emailEditText.text!!.toString().trim()
        password = binding.passwordEditText.text!!.toString().trim()

        checkCredentials()
    }

    private fun checkCredentials() {
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required!"
            binding.emailEditText.requestFocus()
            return
        } else {
            binding.emailEditText.error = null
            binding.emailEditText.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please provide valid email!"
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

        loginUser()
    }

    private fun loginUser() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                Log.i(TAG, "Starting new Intent: MainActivity")

                val navView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                navView?.visibility = View.VISIBLE
                findNavController()
                    .navigate(R.id.action_startFragment_to_upcomingEventsListFragment)
            } else {
                Toast.makeText(context,
                    "Failed to login! Please check your credentials!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ServiceCast")
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}