package com.example.habittracker.repository

import androidx.lifecycle.LiveData
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.model.Habit
import java.util.*
import kotlin.collections.ArrayList


interface HabitsProvider {
    fun getHabit(habitID: UUID): LiveData<HabitEntity>

    fun loadHabits(): LiveData<List<HabitEntity>>

    fun addOrUpdateHabit(habit: HabitEntity)
}

