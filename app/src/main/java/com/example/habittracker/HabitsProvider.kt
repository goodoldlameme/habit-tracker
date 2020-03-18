package com.example.habittracker

import java.util.*
import kotlin.collections.ArrayList


interface HabitsProvider {
    fun getHabit(habitID: UUID): Habit?

    fun loadHabits(): ArrayList<Habit>

    fun addOrUpdateHabits(vararg habit: Habit)
}

