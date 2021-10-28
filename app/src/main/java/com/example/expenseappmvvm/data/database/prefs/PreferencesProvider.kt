package com.example.expenseappmvvm.data.database.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val USER_ID = "UserId"
private const val SHARE_PREFS = "SharedPreferences"

class PreferencesProvider(context: Context) {

    private val preferences = context.getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        preferences.edit().putLong(
            USER_ID,
            userId
        ).apply()
    }

    fun getUserId(): Long? {
        return preferences.getLong(USER_ID, 0L)
    }

    fun discardUserId() {
        preferences.edit().clear().apply()
    }

}