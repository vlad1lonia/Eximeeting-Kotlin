package com.application.vladcelona.eximeeting.data_classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.application.vladcelona.eximeeting.R
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.Exclude

data class User(
    var fullName: String = "", val email: String = "",
    var companyName: String = "", val birthDate: String = "",
    var profileImage: String = "") {

    @Exclude
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["fullName"] = fullName
        result["email"] = email
        result["companyName"] = companyName
        result["birthDate"] = birthDate
        result["profileImage"] = profileImage

        return result
    }
}
