package com.singlelab.lume.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.singlelab.data.model.consts.Const
import java.io.ByteArrayOutputStream


fun Bitmap.toBase64(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

fun String.toBitmap(): Bitmap {
    val decodedBytes: ByteArray = Base64.decode(
        this.substring(this.indexOf(",") + 1),
        Base64.DEFAULT
    )
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun String.generateImageLink(): String {
    return "${Const.BASE_URL}image/get-person-image?imageUid=$this"
}