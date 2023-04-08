package com.shaderock.lunch.blpmini.data.datasource.disk.auth

import android.content.SharedPreferences
import com.google.gson.Gson
import com.shaderock.lunch.blpmini.data.datasource.disk.SharedPreferencesSource
import javax.inject.Inject

class TokenDiskSource @Inject constructor(
    sharedPreferences: SharedPreferences,
    gson: Gson
) : SharedPreferencesSource<TokenDiskModel>(
    TokenDiskModel::class.java,
    sharedPreferences,
    gson,
    id = "token_cache"
)
