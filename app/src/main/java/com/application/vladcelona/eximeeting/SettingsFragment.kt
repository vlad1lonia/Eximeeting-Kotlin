package com.application.vladcelona.eximeeting

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import android.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_PHOTO = 100
private const val TAG = "SettingsFragment"

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

    private lateinit var imageUri: Uri
    private lateinit var selectedImage: Bitmap
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
        profilePicture.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_PHOTO)
        }

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            try {
                imageUri = data!!.data!!
                val imageStream = imageUri.let { activity?.contentResolver?.openInputStream(it) }
                Log.i(TAG, "$imageStream")

                selectedImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(imageStream),
                    640, 640, false)
                profilePicture.setImageBitmap(selectedImage)

                uploadProfileData()
            } catch (e: FileNotFoundException) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                usernameTextView.text = user.fullName
                companyNameTextView.text = user.companyName

                try {
                    val imageBytes = Base64.decode(user.profileImage, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    profilePicture.setImageBitmap(image)

                } catch (exception: Exception) {
                    Log.i(TAG, user.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadProfileData() {
        user.profileImage = bitmapToString()
        val newUserValues: Map<String, Any> = user.toMap()

        databaseReference.child(uid).updateChildren(newUserValues).addOnSuccessListener {
                Toast.makeText(context, "Successfully updated information", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Unable to complete action", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bitmapToString(): String {
        val imageArray = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, imageArray)
        val converted = imageArray.toByteArray()
        return java.util.Base64.getEncoder().encodeToString(converted)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}