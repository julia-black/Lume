package com.singlelab.lume.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import com.singlelab.lume.model.Const
import java.io.ByteArrayOutputStream


fun Bitmap.toBase64(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

fun String.generateImageLink(): String {
    return "${Const.BASE_URL}image/get-person-image?imageUid=$this"
}

fun Uri.getBitmap(contentResolver: ContentResolver?): Bitmap? {
    contentResolver ?: return null
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            contentResolver,
            this
        )
    } else {
        val source = ImageDecoder.createSource(contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }
}