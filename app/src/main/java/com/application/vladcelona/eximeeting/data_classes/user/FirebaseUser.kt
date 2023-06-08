package com.application.vladcelona.eximeeting.data_classes.user

import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.jsonToMap
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToDate

data class FirebaseUser(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val companyName: String = "",
    val birthDate: String = "",
    val profileImage: String = "",
    val position: String = "",
    val phoneNumber: String = "",
    val website: String = "",
    val visitedEvents: String = ""
) {

    /**
     * Method for converting [FirebaseUser] class object from Firestore to [User] type class
     * @return A new instance of [User]
     * @author Balandin (Vladcelona) Vladislav
     */
    fun convert(): User {

        return User(
            id, fullName, email, companyName, stringToDate(birthDate),
            profileImage, position, phoneNumber, website,
            jsonToMap(visitedEvents) as HashMap<String, Boolean>
        )
    }

    /**
     * Method for converting [FirebaseUser] class object to [HashMap] object for
     * pushing data to Cloud Firebase
     * @return A new instance of [HashMap]
     * @author Balandin (Vladcelona) Vladislav
     */
    fun toMap(): HashMap<String, String> {

        val converted: HashMap<String, String> = HashMap()

        converted["id"] = id
        converted["fullName"] = fullName
        converted["email"] = email
        converted["companyName"] = companyName
        converted["birthDate"] = birthDate
        converted["profileImage"] = profileImage
        converted["position"] = position
        converted["phoneNumber"] = phoneNumber
        converted["website"] = website
        converted["visitedEvents"] = visitedEvents

        return converted
    }
}