package com.singlelab.lume.util

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.singlelab.lume.model.profile.ContactPerson

object ContactsUtil {
    fun getContacts(contentResolver: ContentResolver): List<ContactPerson> {
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )
        val contacts = mutableListOf<ContactPerson>()
        cursor?.let {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val contact = ContactPerson(
                        name = cursor.getString(1),
                        phone = cursor.getString(2)
                    )
                    contacts.add(contact)
                }
            }
        }
        cursor?.close()
        return contacts
    }
}