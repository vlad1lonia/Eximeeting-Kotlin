package com.application.vladcelona.eximeeting.account_access

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.FragmentStartBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "StarFragment"

class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentStartBinding

    override fun onStart() {
        super.onStart()

//        findNavController().popBackStack()
//        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//        // Check if user has an account in firebase
//        FirebaseAuth.getInstance().currentUser.let { it?.let { it1 ->
//            databaseReference.child(it1.uid).get().addOnCompleteListener {
//                    task: Task<DataSnapshot> ->
//                if (task.isSuccessful && task.result.exists()) {
//                    Toast.makeText(context,
//                        "You have been logged in successfully", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.upcomingEventListFragment)
//                }
//            }
//        }
//        }
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
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