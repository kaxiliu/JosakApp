package edu.josakapp.proyectoJosakapp.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedString = Base64.decode(base64String, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        null
    }
}