package com.example.habittracker.models.viewmodels

import android.graphics.Color
import com.example.habittracker.models.HabitType
import com.example.habittracker.models.Priority
import com.example.habittracker.models.database.HabitEntity
import com.example.habittracker.models.netcommunication.HabitDto
import java.util.*
import kotlin.random.Random

class EditHabit(var name: String? = null,
                var description: String? = null,
                var priority: Priority? = null,
                var type: HabitType? = null,
                var count: Int? = null,
                var period: Int? = null,
                var color: Int? = null) {

    companion object {
        fun fromHabitEntity(habit: HabitEntity): EditHabit {
            return EditHabit(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.count,
                habit.period,
                habit.color)
        }
    }

    fun toHabitDto(habitId: UUID?): HabitDto {
        return HabitDto(
            color ?: Color.argb(
            255,
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)),
            count ?: 0,
            Calendar.getInstance().timeInMillis,
            description.toString(),
            period ?: 0,
            priority ?: Priority.Medium,
            name.toString(),
            type ?: HabitType.Good,
            habitId?.toString()
        )
    }
}