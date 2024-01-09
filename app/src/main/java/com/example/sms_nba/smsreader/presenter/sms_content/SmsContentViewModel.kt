package com.example.sms_nba.smsreader.presenter.sms_content

import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sms_nba.smsreader.data.SmsChatEntry

class SmsContentViewModel : ViewModel() {
    private val _chatEntries = MutableLiveData<List<SmsChatEntry>>()
    val chatEntries: LiveData<List<SmsChatEntry>>
        get() = _chatEntries

    fun loadSmsMessages(contentResolver: ContentResolver, addressId: String) {
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            "${Telephony.Sms.ADDRESS} = ?",
            arrayOf(addressId),
            null
        )

        val chatEntriesList = _chatEntries.value?.toMutableList() ?: mutableListOf()

        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))

                    // Проверяем, существует ли SmsChatEntry с текущим адресом в списке
                    val existingEntry = chatEntriesList.find { it.adress == address }
                    if (existingEntry != null) {
                        // Если существует, обновляем существующий SmsChatEntry в списке
                        val updatedEntry = existingEntry.copy(messages = existingEntry.messages + body)
                        chatEntriesList.remove(existingEntry)
                        chatEntriesList.add(updatedEntry)
                    } else {
                        // Если не существует, создаем новый SmsChatEntry
                        val newEntry = SmsChatEntry(address, listOf(body))
                        chatEntriesList.add(newEntry)
                    }
                } while (cursor.moveToNext())
            }
        }

        cursor?.close()

        _chatEntries.postValue(chatEntriesList)
    }
}