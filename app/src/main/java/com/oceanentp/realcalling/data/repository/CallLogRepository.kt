package com.oceanentp.realcalling.data.repository

import android.content.Context
import android.provider.CallLog.Calls
import com.oceanentp.realcalling.data.model.CallLogEntry
import com.oceanentp.realcalling.util.PermissionChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CallLogRepository(private val context: Context) {

    fun getCallLogs(): Flow<List<CallLogEntry>> = flow {
        if (!PermissionChecker.hasReadCallLogPermission(context)) {
            emit(emptyList())
            return@flow
        }

        val callLogs = mutableListOf<CallLogEntry>()
        val uri = Calls.CONTENT_URI
        val projection = arrayOf(
            Calls.NUMBER,
            Calls.CACHED_NAME,
            Calls.DATE,
            Calls.DURATION,
            Calls.TYPE
        )

        context.contentResolver.query(uri, projection, null, null, Calls.DEFAULT_SORT_ORDER)?.use { cursor ->
            val numberIndex = cursor.getColumnIndexOrThrow(Calls.NUMBER)
            val nameIndex = cursor.getColumnIndexOrThrow(Calls.CACHED_NAME)
            val dateIndex = cursor.getColumnIndexOrThrow(Calls.DATE)
            val durationIndex = cursor.getColumnIndexOrThrow(Calls.DURATION)
            val typeIndex = cursor.getColumnIndexOrThrow(Calls.TYPE)

            while (cursor.moveToNext()) {
                callLogs.add(
                    CallLogEntry(
                        number = cursor.getString(numberIndex) ?: "",
                        name = cursor.getString(nameIndex),
                        date = cursor.getLong(dateIndex),
                        duration = cursor.getLong(durationIndex),
                        type = cursor.getInt(typeIndex)
                    )
                )
            }
        }

        emit(callLogs)
    }
}
