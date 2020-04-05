package com.example.habittracker.repository

import com.example.habittracker.models.Habit
import com.example.habittracker.common.replaceOrAdd
import java.util.*
import kotlin.collections.ArrayList

object LocalHabitsProvider: HabitsProvider {
    private var habits: ArrayList<Habit> = ArrayList()
    override fun getHabit(habitID: UUID): Habit? {
        return habits.find { it.id == habitID }
    }

    override fun loadHabits(): ArrayList<Habit> {
        return habits
    }

    override fun addOrUpdateHabits(vararg habitsToAdd: Habit) {
        habits = habitsToAdd.fold(
            habits
        ) { acc, habit -> acc.replaceOrAdd(habit){it.id == habit.id} }
    }
}