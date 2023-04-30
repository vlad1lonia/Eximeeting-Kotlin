package com.application.vladcelona.eximeeting.navigation

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.databinding.FragmentSettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


private const val TAG = "SettingsFragment"

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_PHOTO = 100
private const val REQUEST_CROP_PHOTO = 200


class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var user: User
    private lateinit var uid: String

    private lateinit var imageUri: Uri
    private lateinit var selectedImage: Bitmap
    private lateinit var databaseReference: DatabaseReference
//    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.profilePicture.setImageResource(R.drawable.person_icon)
        binding.profilePicture.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_PHOTO)
        }

        binding.personalButton.setOnClickListener {
            findNavController().navigate(R.id.personalInfoFragment)
        }

        binding.appearanceButton.setOnClickListener {
            findNavController().navigate(R.id.appearanceFragment)
        }

        binding.businessCardButton.setOnClickListener {
            findNavController().navigate(R.id.businessCardFragment)
        }

        binding.appInformationButton.setOnClickListener {
            findNavController().navigate(R.id.appInfoFragment)
        }

        binding.signOutButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
            alertDialog.setTitle("Are you sure you want to sign out?")

            alertDialog.setPositiveButton("Sign out") { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()

                val navView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                navView?.visibility = View.INVISIBLE
//                findNavController().navigateUp()
                findNavController().navigate(R.id.action_settingsFragment_to_startFragment)
            }
            alertDialog.setNegativeButton("No", null)

            alertDialog.show()
        }

        if (uid.isNotEmpty()) { getUserData() }
        setViewVisibility(false)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            try {
                imageUri = data!!.data!!
                val imageStream = imageUri.let { activity?.contentResolver?.openInputStream(it) }
                Log.i(TAG, "$imageStream")

                val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
                Log.i(TAG, "${selectedImageBitmap.height}, ${selectedImageBitmap.width}")

                val width = 640
                val height = (width / (selectedImageBitmap.width.toDouble() /
                        selectedImageBitmap.height)).toInt()

                selectedImage = Bitmap.createScaledBitmap(selectedImageBitmap, 640, height, false)
                binding.profilePicture.setImageBitmap(selectedImage)

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
                binding.usernameTextView.text = user.fullName
                binding.companyNameTextView.text = user.companyName

                try {
                    val imageBytes = Base64.decode(user.profileImage, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    binding.profilePicture.setImageBitmap(image)

                    setViewVisibility(true)

                } catch (exception: Exception) {
                    Toast.makeText(
                        context, "Failed to download image from database", Toast.LENGTH_SHORT
                    ).show()

                    setViewVisibility(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    activity, "Failed to download data from Database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun uploadProfileData() {
        user.profileImage = bitmapToString()
        val newUserValues: Map<String, Any> = user.toMap()

        databaseReference.child(uid).updateChildren(newUserValues).addOnSuccessListener {
            Toast.makeText(
                context, "Successfully updated information",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                context, "Unable to complete action",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun bitmapToString(): String {
        val imageArray = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, imageArray)
        val converted = imageArray.toByteArray()
        return java.util.Base64.getEncoder().encodeToString(converted)
    }

    private fun setViewVisibility(visible: Boolean) {
        when (visible) {
            true -> {
                binding.signOutButton.visibility = View.VISIBLE

                binding.personalButton.visibility = View.VISIBLE
                binding.appearanceButton.visibility = View.VISIBLE
                binding.businessCardButton.visibility = View.VISIBLE
                binding.appInformationButton.visibility = View.VISIBLE
                binding.profilePicture.visibility = View.VISIBLE
                binding.profileImageEdit.visibility = View.VISIBLE

                binding.usernameTextView.visibility = View.VISIBLE
                binding.companyNameTextView.visibility = View.VISIBLE

                binding.splitButton1.visibility = View.VISIBLE
                binding.splitButton2.visibility = View.VISIBLE
                binding.splitButton3.visibility = View.VISIBLE

                binding.progressBar.visibility = View.INVISIBLE
            }
            false -> {
                binding.signOutButton.visibility = View.INVISIBLE

                binding.personalButton.visibility = View.INVISIBLE
                binding.appearanceButton.visibility = View.INVISIBLE
                binding.businessCardButton.visibility = View.INVISIBLE
                binding.appInformationButton.visibility = View.INVISIBLE

                binding.profilePicture.visibility = View.INVISIBLE
                binding.profileImageEdit.visibility = View.INVISIBLE

                binding.usernameTextView.visibility = View.INVISIBLE
                binding.companyNameTextView.visibility = View.INVISIBLE

                binding.splitButton1.visibility = View.INVISIBLE
                binding.splitButton2.visibility = View.INVISIBLE
                binding.splitButton3.visibility = View.INVISIBLE

                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}