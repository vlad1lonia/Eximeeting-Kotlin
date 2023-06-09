package com.application.vladcelona.eximeeting.data_classes.user

import android.graphics.Bitmap
import android.graphics.Color
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.birthDateToString
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.objectToJson
import com.google.firebase.firestore.Exclude
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Date

/**
 * Data class which contains information about a user who has an account on Firebase
 * @author Balandin (Vladcelona) Vladislav
 */
data class User(
    val id: String = "",
    var fullName: String = "",
    val email: String = "",
    var companyName: String = "",
    val birthDate: Date = Date(),
    var profileImage: String = "",
    var position: String = "",
    var phoneNumber: String = "",
    var website: String = "",
    var visitedEvents: HashMap<String, Boolean> = HashMap()
) {

    /**
     * Method for converting the [User] data class object into the Map
     * @return A new instance of Map with String type key and Any type object
     * @author Balandin (Vladcelona) Vladislav
     */
    @Exclude
    fun toMap(): Map<String, Any> {
        val convertedUser: HashMap<String, Any> = HashMap()
        convertedUser["fullName"] = fullName
        convertedUser["email"] = email
        convertedUser["companyName"] = companyName
        convertedUser["birthDate"] = birthDate
        convertedUser["profileImage"] = profileImage
        convertedUser["position"] = position
        convertedUser["phoneNumber"] = phoneNumber
        convertedUser["website"] = website
        convertedUser["visitedEvents"] = visitedEvents

        return convertedUser
    }

    /**
     * Method for converting the [User] url link into QR-code
     * @return A new instance of [Bitmap] which contains the QR-code of the url
     * @author Balandin (Vladcelona) Vladislav
     */
    fun convertToQRCode(): Bitmap {
        val writer = QRCodeWriter()

        val bitMatrix: BitMatrix = writer.encode(website, BarcodeFormat.QR_CODE, 512, 512)
        val width: Int = bitMatrix.width
        val height: Int = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    /**
     * Method for converting [User] class object to [FirebaseUser] class object
     * @return A new instance of [FirebaseUser]
     * @author Balandin (Vladcelona) Vladislav
     */
    fun convert(): FirebaseUser {

        return FirebaseUser(
            id.toString(), fullName, email, companyName, birthDateToString(birthDate),
            profileImage, position, phoneNumber, website, objectToJson(visitedEvents)
        )
    }
}