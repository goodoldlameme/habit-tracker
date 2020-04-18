package com.example.habittracker.netcommunication

import android.util.Log
import com.example.habittracker.common.Strings
import com.example.habittracker.models.netcommunication.HabitDto
import com.example.habittracker.models.netcommunication.HabitId
import com.example.habittracker.models.netcommunication.typeadapters.HabitDtoJsonDeserializer
import com.example.habittracker.models.netcommunication.typeadapters.HabitDtoJsonSerializer
import com.example.habittracker.models.netcommunication.typeadapters.HabitIdJsonDeserializer
import com.example.habittracker.models.netcommunication.typeadapters.HabitIdJsonSerializer
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface HabitsRemoteService {
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDto>>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: HabitDto): Response<HabitDto>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body habitId: HabitId): Response<Void>

    companion object{
        const val LOG_TAG = "HabitsRemoteService"

        suspend fun <T> retry(request: suspend () -> Response<T>): Response<T>? {
            val retryCount = 5
            var tries = 0
            var errorCode: Int? = null
            var responseBody: ResponseBody? = null
            do {
                try {
                    val response = request()
                    if (response.isSuccessful) {
                        return response
                    } else {
                        errorCode = response.code()
                        responseBody = response.errorBody()
                        Log.e(LOG_TAG, response.message())
                        break
                    }
                } catch (e: Exception) {
                    tries++
                    delay(2000)
                }
            } while (tries < retryCount)

            return errorCode?.let{code ->
                Response.error(code, responseBody!!)
            }
        }

        fun getInstance(): HabitsRemoteService{
            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(AuthRequestInterceptor(AuthenticationManager.getApiToken()))
                .build()

            val gson = GsonBuilder()
                .registerTypeAdapter(HabitDto::class.java, HabitDtoJsonSerializer())
                .registerTypeAdapter(HabitDto::class.java, HabitDtoJsonDeserializer())
                .registerTypeAdapter(HabitId::class.java, HabitIdJsonSerializer())
                .registerTypeAdapter(HabitId::class.java, HabitIdJsonDeserializer())
                .create()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Strings.RemoteApiUrl)
                .build()

            return retrofit.create(HabitsRemoteService::class.java)
        }
    }
}