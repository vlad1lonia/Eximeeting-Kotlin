package com.application.vladcelona.eximeeting.account_access

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.FragmentStartBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "StartFragment"

// TODO: Change Realtime Database for Firestore
class StartFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentStartBinding

    override fun onStart() {
        super.onStart()

        val navView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.startFragment, true).build()

        // TODO: Change Realtime Database for Firestore
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        FirebaseAuth.getInstance().currentUser.let { it?.let {it1 ->
            databaseReference.child(it1.uid)
                .get().addOnCompleteListener {task: Task<DataSnapshot> ->
                    if (task.isSuccessful && task.result.exists()) {
                        Toast.makeText(context, "You have been logged in successfully",
                            Toast.LENGTH_SHORT).show()
                        navView?.visibility = View.VISIBLE
                        findNavController()
                            .navigate(
                                R.id.action_startFragment_to_upcomingEventsListFragment,
                                bundleOf(), navOptions
                            )
                    }
            }
        } }
    }

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
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            Log.i(TAG, "Creating LoginActivity")
            findNavController().navigate(R.id.loginFragment)
        }

        binding.registerButton.setOnClickListener {
            Log.i(TAG, "Creating RegisterActivity")
            findNavController().navigate(R.id.registerFragment)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}