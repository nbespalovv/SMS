package com.example.sms_nba.smsreader.presenter.sms_list

import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sms_nba.smsreader.data.SmsChatEntry
import kotlinx.coroutines.processNextEventInCurrentThread

class SmsListViewModel : ViewModel() {
    private val _chatMessageEntries = MutableLiveData<List<SmsChatEntry>>()
    val chatMessageEntries: LiveData<List<SmsChatEntry>>
        get() = _chatMessageEntries

    fun loadSmsMessages(contentResolver: ContentResolver) {
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val result = AddressAndMessageList(mutableListOf())

        if (cursor?.moveToFirst() == true) {
            do {
                val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                result.list.add(address to body)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        _chatMessageEntries.postValue(result.toSmsChatEntriesList())
    }
}

@JvmInline
value class AddressAndMessageList(
    val list: MutableList<Pair<String, String>>
) {
    fun toSmsChatEntriesList() : List<SmsChatEntry> =
        list.groupBy { it.first }
            .map {
                val messages = it.value.map { it.second }
                SmsChatEntry(it.key, messages)
            }
}