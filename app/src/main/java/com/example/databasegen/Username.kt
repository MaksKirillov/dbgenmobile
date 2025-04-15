package com.example.databasegen

import android.content.Context
import androidx.core.content.edit

fun saveUsername(context: Context, username: String) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit() {
        putString("username", username)
    } // или editor.commit() для синхронного сохранения
}

fun getUsername(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("username", null) // null - значение по умолчанию
}