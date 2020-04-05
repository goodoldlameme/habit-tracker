package com.example.habittracker.common

import androidx.annotation.StringRes
import com.example.habittracker.App

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }

    const val HABIT_EDIT_CREATE = "HABIT_EDIT_CREATE"
    const val HABIT_EDIT_POSITION = "HABIT_EDIT_POSITION"
    const val START_CREATE_HABIT_ACTIVITY_REQUEST_CODE = 0
    const val START_EDIT_HABIT_ACTIVITY_REQUEST_CODE = 1
}

