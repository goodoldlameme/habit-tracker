package com.example.habittracker.repository

import androidx.lifecycle.LiveData
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.models.Habit
import java.util.*
import kotlin.collections.ArrayList


interface HabitsProvider {
    fun getHabit(habitID: UUID?): LiveData<HabitEntity>

    fun loadHabits(): LiveData<List<HabitEntity>>

    suspend fun addOrUpdateHabit(habitEntity: HabitEntity)

    suspend fun deleteHabit(habitEntity: HabitEntity)
}

