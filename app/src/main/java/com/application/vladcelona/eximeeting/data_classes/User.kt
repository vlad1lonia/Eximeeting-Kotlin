package com.application.vladcelona.eximeeting.data_classes

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.application.vladcelona.eximeeting.R
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.Exclude
import com.google.firebase.ktx.Firebase

data class User(
    var fullName: String = "", val email: String = "",
    var companyName: String = "", val birthDate: String = "", var profileImage: String = "",
    val position: String = "Not mentioned",
    val phoneNumber: String = "Not mentioned",
    val website: String = "Not mentioned") {

    @Exclude
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["fullName"] = fullName
        result["email"] = email
        result["companyName"] = companyName
        result["birthDate"] = birthDate
        result["profileImage"] = profileImage
        result["position"] = position
        result["phoneNumber"] = phoneNumber
        result["website"] = website

        return result
    }

    companion object {
        fun checkAccess(): Boolean {
            return when (Firebase.auth.currentUser) {
                null -> false
                else -> true
            }
        }
    }
}