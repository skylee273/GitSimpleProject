package com.example.gitsimpleproject.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class AuthTokenProvider(private val context: Context?) {

    fun updateToken(@NonNull token: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    @Nullable
    fun getToken(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_AUTH_TOKEN, null)
    }

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }
}