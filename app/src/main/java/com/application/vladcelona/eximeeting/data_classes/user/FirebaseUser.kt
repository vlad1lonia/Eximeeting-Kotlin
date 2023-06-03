package com.application.vladcelona.eximeeting.data_classes.user

import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToDate
import com.google.gson.Gson
import java.util.UUID

data class FirebaseUser(
    val id: String = "",
    var fullName: String = "",
    val email: String = "",
    var companyName: String = "",
    val birthDate: String = "",
    var profileImage: String = "",
    var position: String = "",
    var phoneNumber: String = "",
    var website: String = "",
    var visitedEvents: String = ""
) {

    /**
     * Method for converting [FirebaseUser] data class from Firestore to [User] data class
     * @return A new instance of [User]
     */
    fun convert(): User {

        return User(
            UUID.fromString(id), fullName,
            email, companyName, stringToDate(birthDate),
            profileImage, position, phoneNumber, website,
            Gson().fromJson(visitedEvents, HashMap<String, Boolean>()::class.java)
        )
    }
}