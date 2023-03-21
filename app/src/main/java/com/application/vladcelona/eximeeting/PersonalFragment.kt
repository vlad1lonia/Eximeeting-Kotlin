package com.application.vladcelona.eximeeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "PersonalFragment"
class PersonalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editedFullName: String
    private lateinit var editedCompanyName: String

    private lateinit var fullNameEditText: EditText
    private lateinit var companyNameEditText: EditText

    private lateinit var updateInformationButton: Button

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
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_personal, container, false)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (uid.isNotEmpty()) { getUserData() }

        fullNameEditText = view.findViewById(R.id.full_name_edit_text)
        companyNameEditText = view.findViewById(R.id.company_name_edit_text)

        updateInformationButton = view.findViewById(R.id.update_information_button)

        updateInformationButton.setOnClickListener { updateInformation() }

        return view;
    }

    private fun updateInformation() {
        editedFullName = fullNameEditText.text.toString()
        editedCompanyName = companyNameEditText.text.toString()

        checkCredentials()
    }

    private fun checkCredentials() {
        if (editedFullName.isEmpty()) {
            fullNameEditText.error = "Full name is required!"
            fullNameEditText.requestFocus()
            return
        } else {
            fullNameEditText.error = null
            fullNameEditText.requestFocus()
        }

        if (editedCompanyName.isEmpty()) {
            companyNameEditText.error = "Company name is required!"
            companyNameEditText.requestFocus()
            return
        } else {
            companyNameEditText.error = null
            companyNameEditText.requestFocus()
        }

        updateFirebase()
    }

    private fun updateFirebase() {
        if (editedFullName.isNotEmpty()) { user.fullName = editedFullName }
        if (editedCompanyName.isNotEmpty()) { user.companyName = editedCompanyName }

        val newUserValues: Map<String, Any> = user.toMap()

        databaseReference.child(uid).updateChildren(newUserValues).addOnSuccessListener {
            Toast.makeText(context, "Successfully updated information", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Unable to complete action", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                fullNameEditText.hint = user.fullName
                companyNameEditText.hint = user.companyName
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }
}