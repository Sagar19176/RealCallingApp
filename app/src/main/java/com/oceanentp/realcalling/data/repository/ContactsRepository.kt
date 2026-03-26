package com.oceanentp.realcalling.data.repository

import android.content.Context
import android.provider.ContactsContract
import com.oceanentp.realcalling.data.model.Contact
import com.oceanentp.realcalling.util.PermissionChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContactsRepository(private val context: Context) {

    fun getContacts(): Flow<List<Contact>> = flow {
        if (!PermissionChecker.hasReadContactsPermission(context)) {
            emit(emptyList())
            return@flow
        }

        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                contacts.add(
                    Contact(
                        id = cursor.getString(idIndex),
                        name = cursor.getString(nameIndex) ?: "",
                        phoneNumber = cursor.getString(numberIndex) ?: ""
                    )
                )
            }
        }

        emit(contacts)
    }
}
