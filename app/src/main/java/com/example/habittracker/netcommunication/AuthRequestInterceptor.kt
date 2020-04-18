package com.example.habittracker.netcommunication

import okhttp3.Interceptor
import okhttp3.Response

class AuthRequestInterceptor(private val apiToken: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header(
            "Authorization",
            apiToken
        )
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}