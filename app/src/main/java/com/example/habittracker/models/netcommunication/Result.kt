package com.example.habittracker.models.netcommunication

data class ErrorResponse(val code: Int, val message: String)

data class Result<TValue>(
    val error: ErrorResponse? = null,
    val result: TValue? = null
)