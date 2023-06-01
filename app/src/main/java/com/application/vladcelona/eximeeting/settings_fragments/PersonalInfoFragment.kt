package com.application.vladcelona.eximeeting.settings_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.application.vladcelona.eximeeting.databinding.FragmentPersonalInfoBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "PersonalInfoFragment"


// TODO: Change Realtime Database for Firestore
@Suppress("DEPRECATION")
class PersonalInfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentPersonalInfoBinding

    private lateinit var editedFullName: String
    private lateinit var editedCompanyName: String

    private lateinit var databaseReference: DatabaseReference

    private lateinit var user: User
    private lateinit var uid: String

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
        // Inflate the layout for this fragment
        binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (uid.isNotEmpty()) { getUserData() }

        binding.passwordResetTextView.setOnClickListener {
            binding.oldPasswordGroup.visibility = View.VISIBLE
            binding.newPasswordGroup.visibility = View.VISIBLE
        }

        binding.updateInformationButton.setOnClickListener { updateInformation() }

        return binding.root
    }

    // Do not update this method
    private fun updateInformation() {
        editedFullName = binding.fullNameEditText.text.toString()
        editedCompanyName = binding.companyNameEditText.text.toString()

        checkCredentials()
    }

    // Do not update this method
    private fun checkCredentials() {
        if (editedFullName.isEmpty()) {
            editedFullName = user.fullName
        }

        if (editedCompanyName.isEmpty()) {
            editedCompanyName = user.companyName
        }

        if (binding.oldPasswordGroup.visibility == View.INVISIBLE
            && binding.newPasswordGroup.visibility == View.INVISIBLE) {
            updateFirebase()
        } else {
            checkPasswordReset()
        }
    }

    // Do not update this method!
    private fun checkPasswordReset() {
        val oldPassword = binding.oldPasswordEditText.text.toString().trim()
        val newPassword = binding.newPasswordEditText.text.toString().trim()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, oldPassword)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.i(TAG, "Successfully updated password")
                                updateFirebase()
                            }
                        }
                } else {
                    Toast.makeText(context,
                        "Failed to login! Please check your credentials!",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    // TODO: Change Realtime Database for Firestore
    private fun updateFirebase() {
        if (editedFullName.isNotEmpty()) { user.fullName = editedFullName }
        if (editedCompanyName.isNotEmpty()) { user.companyName = editedCompanyName }

        val newUserValues: Map<String, Any> = user.toMap()

        databaseReference.child(uid).updateChildren(newUserValues).addOnSuccessListener {
            Toast.makeText(context, "Successfully updated information", Toast.LENGTH_SHORT).show()
            fragmentManager?.popBackStack()
        }.addOnFailureListener {
            Toast.makeText(context, "Unable to complete action", Toast.LENGTH_SHORT).show()
        }
    }

    // TODO: Change Realtime Database for Firestore
    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.fullNameEditText.hint = user.fullName
                binding.companyNameEditText.hint = user.companyName
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}