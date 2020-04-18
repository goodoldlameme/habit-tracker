package com.example.habittracker.repository

import androidx.lifecycle.LiveData
import com.example.habittracker.models.database.HabitEntity
import com.example.habittracker.models.netcommunication.HabitDto
import java.util.*

interface HabitsProvider {
    fun getHabit(habitID: UUID?): LiveData<HabitEntity>

    fun getHabits(): LiveData<List<HabitEntity>>

    suspend fun loadHabitsFromRemote()

    suspend fun addOrUpdateHabit(habitDto: HabitDto)

    suspend fun deleteHabit(habitEntity: HabitEntity): Boolean
}

