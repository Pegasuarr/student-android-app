package com.example.frontend.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "student_app_prefs"
        private const val USER_TOKEN = "user_token"
        private const val USER_ROLE = "user_role"
        private const val USERNAME = "username"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserRole(role: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROLE, role)
        editor.apply()
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun fetchUsername(): String? {
        return prefs.getString(USERNAME, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_ROLE)
        editor.remove(USERNAME)
        editor.apply()
    }
}
