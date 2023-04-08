package com.shaderock.lunch.blpmini.data.datasource.disk

import android.content.SharedPreferences
import com.google.gson.Gson

abstract class SharedPreferencesSource<T>(
    private val dataClass: Class<T>,
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson,
    private val id: String
) {
    var data: T?
        get() = gson.fromJson(sharedPrefs.getString(id, null), dataClass)
        set(value) = sharedPrefs.edit().run {
            if (value == null) remove(id) else putString(id, gson.toJson(value)).apply()
            apply()
        }
}
