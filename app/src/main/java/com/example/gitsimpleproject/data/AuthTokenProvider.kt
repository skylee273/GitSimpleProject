package com.example.gitsimpleproject.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class AuthTokenProvider(applicationContext: Context?) {

    private val KEY_AUTH_TOKEN = "auth_token"

    private var context: Context? = null

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

    init {
        this.context = context
    }
}