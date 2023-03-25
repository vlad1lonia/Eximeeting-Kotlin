package com.application.vladcelona.eximeeting.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.data_classes.User
import com.application.vladcelona.eximeeting.login_register.PickActivity
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

    // Necessary Views

    private lateinit var signOutButton: Button

    private lateinit var personalButton: Button
    private lateinit var appearanceButton: Button
    private lateinit var middleButton2: Button
    private lateinit var appInfoButton: Button

    private lateinit var profilePicture: ImageView
    private lateinit var profileImageEdit: ImageView

    private lateinit var usernameTextView: TextView
    private lateinit var companyNameTextView: TextView

    private lateinit var progressBar: ProgressBar

    // Unnecessary Views

    private lateinit var splitButton1: Button
    private lateinit var splitButton2: Button
    private lateinit var splitButton3: Button

    //

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

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        // Main View in fragment
        progressBar = view.findViewById(R.id.progress_bar) as ProgressBar

        signOutButton = view.findViewById(R.id.sign_out_button) as Button

        personalButton = view.findViewById(R.id.personal_button) as Button
        appearanceButton = view.findViewById(R.id.appearance_button) as Button
        middleButton2 = view.findViewById(R.id.middle_button_2) as Button
        appInfoButton = view.findViewById(R.id.app_information_button) as Button

        profilePicture = view.findViewById(R.id.profile_picture) as ImageView
        profileImageEdit = view.findViewById(R.id.profile_image_edit) as ImageView

        usernameTextView = view.findViewById(R.id.username) as TextView
        companyNameTextView = view.findViewById(R.id.company_name) as TextView

        profilePicture.setImageResource(R.drawable.person_icon)
        profilePicture.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_PHOTO)
        }

        personalButton.setOnClickListener {

            for (fragment in activity?.supportFragmentManager?.fragments!!) {
                activity?.supportFragmentManager?.beginTransaction()?.hide(fragment)?.commit()
            }

            activity?.supportFragmentManager?.beginTransaction()
                ?.hide(this@SettingsFragment)?.add(
                    R.id.fragment_container,
                    PersonalFragment.newInstance(), null)?.addToBackStack(null)?.commit()
        }

        appearanceButton.setOnClickListener {
            Toast.makeText(context, "Sorry, this button is currently unavailable!",
                Toast.LENGTH_SHORT).show()
        }

        middleButton2.setOnClickListener {
            Toast.makeText(context, "Sorry, this button is currently unavailable!",
                Toast.LENGTH_SHORT).show()
        }

        appInfoButton.setOnClickListener {

//            Toast.makeText(context, "Sorry, this button is currently unavailable!",
//                Toast.LENGTH_SHORT).show()

            for (fragment in activity?.supportFragmentManager?.fragments!!) {
                activity?.supportFragmentManager?.beginTransaction()?.hide(fragment)?.commit()
            }

            activity?.supportFragmentManager?.beginTransaction()
                ?.hide(this@SettingsFragment)?.add(
                    R.id.fragment_container,
                    AppInfoFragment.newInstance(), null)?.addToBackStack(null)?.commit()
        }

        signOutButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
            alertDialog.setTitle("Are you sure you want to sign out?")

            alertDialog.setPositiveButton("Sign out") { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, PickActivity::class.java)
                val bundle = Bundle(); bundle.putBoolean("login_state", false)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            alertDialog.setNegativeButton("No", null)

            alertDialog.show()
        }

        // Additional and unnecessary Views in fragment
        splitButton1 = view.findViewById(R.id.split_button_1)
        splitButton2 = view.findViewById(R.id.split_button_2)
        splitButton3 = view.findViewById(R.id.split_button_3)

        if (uid.isNotEmpty()) { getUserData() }

        setViewVisibility(false)

        return view
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

                    setViewVisibility(true)

                } catch (exception: Exception) {
                    Toast.makeText(
                        context,
                        "Failed to download image from database", Toast.LENGTH_SHORT
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
                signOutButton.visibility = View.VISIBLE

                personalButton.visibility = View.VISIBLE
                appearanceButton.visibility = View.VISIBLE
                middleButton2.visibility = View.VISIBLE
                appInfoButton.visibility = View.VISIBLE

                profilePicture.visibility = View.VISIBLE
                profileImageEdit.visibility = View.VISIBLE

                usernameTextView.visibility = View.VISIBLE
                companyNameTextView.visibility = View.VISIBLE

                splitButton1.visibility = View.VISIBLE
                splitButton2.visibility = View.VISIBLE
                splitButton3.visibility = View.VISIBLE

                progressBar.visibility = View.INVISIBLE
            }
            false -> {
                signOutButton.visibility = View.INVISIBLE

                personalButton.visibility = View.INVISIBLE
                appearanceButton.visibility = View.INVISIBLE
                middleButton2.visibility = View.INVISIBLE
                appInfoButton.visibility = View.INVISIBLE

                profilePicture.visibility = View.INVISIBLE
                profileImageEdit.visibility = View.INVISIBLE

                usernameTextView.visibility = View.INVISIBLE
                companyNameTextView.visibility = View.INVISIBLE

                splitButton1.visibility = View.INVISIBLE
                splitButton2.visibility = View.INVISIBLE
                splitButton3.visibility = View.INVISIBLE

                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH
        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}