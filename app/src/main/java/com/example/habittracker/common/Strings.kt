package com.example.habittracker.common

import androidx.annotation.StringRes
import com.example.habittracker.App

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }

    const val RemoteApiUrl = "https://droid-test-server.doubletapp.ru/api/"
}

