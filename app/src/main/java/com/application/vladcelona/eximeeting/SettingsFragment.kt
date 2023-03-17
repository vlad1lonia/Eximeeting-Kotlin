package com.application.vladcelona.eximeeting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.login_register.PickActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_PHOTO = 100

class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var signOutButton: Button
    private lateinit var appearanceButton: Button
    private lateinit var personalButton: Button

    private lateinit var profilePicture: ImageView

    private lateinit var usernameTextView: TextView
    private lateinit var companyNameTextView: TextView

    private lateinit var user: User
    private lateinit var uid: String

    private lateinit var photoUri: Uri
    private lateinit var databaseReference: DatabaseReference
//    private lateinit var storageReference: StorageReference

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
//        return inflater.inflate(R.layout.fragment_settings, container, false)

        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        signOutButton = view.findViewById(R.id.sign_out_button)
        appearanceButton = view.findViewById(R.id.appearance_button)
        personalButton = view.findViewById(R.id.personal_button)

        profilePicture = view.findViewById(R.id.profile_picture)

        // TODO: Replace this with getting information from Firebase Realtime Database
        profilePicture.setImageResource(R.drawable.person_icon)

        usernameTextView = view.findViewById(R.id.username)
        companyNameTextView = view.findViewById(R.id.company_name)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (uid.isNotEmpty()) { getUserData() }

        personalButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container, PersonalFragment.newInstance())?.commit()
        }

        signOutButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
            alertDialog.setTitle("Are you sure you want to sign out?")

            alertDialog.setPositiveButton("Sign out") { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, PickActivity::class.java))
            }
            alertDialog.setNegativeButton("No", null)

            alertDialog.show()
        }

        return view
    }

    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                usernameTextView.text = user.fullName
                companyNameTextView.text = user.companyName
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}