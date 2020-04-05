package com.example.habittracker.repository

import com.example.habittracker.models.Habit
import java.util.*
import kotlin.collections.ArrayList


interface HabitsProvider {
    fun getHabit(habitID: UUID): Habit?

    fun loadHabits(): ArrayList<Habit>

    fun addOrUpdateHabits(vararg habit: Habit)
}

