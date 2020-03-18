package com.example.habittracker

import android.graphics.Color
import java.util.*
import kotlin.random.Random

class EditHabit(var name: String? = null,
                var description: String? = null,
                var priority: Int? = null,
                var type: HabitType? = null,
                var count: Int? = null,
                var period: Int? = null,
                var color: Int? = null,
                var creationDate: Calendar? = null) {

    companion object {
        fun fromHabit(habit: Habit): EditHabit {
            return EditHabit(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.count,
                habit.period,
                habit.color,
                habit.creationDate
            )
        }
    }

    fun toHabit(habitId: UUID?): Habit{
        return Habit(
            name.toString(),
            description.toString(),
            priority ?: 1,
            type ?: HabitType.Good,
            count ?: 0,
            period ?: 0,
            color ?: Color.argb(255,
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)),
            habitId ?: UUID.randomUUID(),
            creationDate ?: Calendar.getInstance()
        )
    }
}