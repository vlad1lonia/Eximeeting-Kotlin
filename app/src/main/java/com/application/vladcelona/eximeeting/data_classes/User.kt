package com.application.vladcelona.eximeeting.data_classes

import android.graphics.Bitmap
import android.graphics.Color
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.Exclude
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

data class User(
    var fullName: String = "", val email: String = "",
    var companyName: String = "", val birthDate: String = "", var profileImage: String = "",
    val position: String = "Not mentioned",
    val phoneNumber: String = "Not mentioned",
    val website: String = "https://vk.com/vladcelona"
) {

    /**
     * Method for converting the User data class object into the Map
     * @return A new instance of Map with String type key and Any type object
     */
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

    /**
     * Method for converting the User url link into QR-code
     * @return A new instance of Bitmap which contains the QR-code of the url
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

    companion object {
        fun checkAccess(): Boolean {
            return when (Firebase.auth.currentUser) {
                null -> false
                else -> true
            }
        }
    }
}