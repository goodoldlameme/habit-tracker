package com.example.habittracker.netcommunication

import com.example.habittracker.R
import com.example.habittracker.common.Strings

object AuthenticationManager {
    private val apiToken: String? = null
    fun getApiToken(): String{
        if (apiToken != null)
            return apiToken
        else
            return Strings.get(R.string.apikey)
    }
}