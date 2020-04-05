package com.example.habittracker.model

import android.graphics.Color
import com.example.habittracker.database.HabitEntity
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
        fun fromHabitEntity(habit: HabitEntity): EditHabit {
            return EditHabit(
                habit.name,
                habit.description,
                habit.priority,
                HabitType.valueOf(habit.type),
                habit.count,
                habit.period,
                habit.color,
                habit.creationDate
            )
        }
    }

    fun toHabitEntity(habitId: UUID?): HabitEntity {
        return HabitEntity(
            name.toString(),
            description.toString(),
            priority ?: 1,
            type?.toString() ?: HabitType.Good.toString(),
            count ?: 0,
            period ?: 0,
            color ?: Color.argb(
                255,
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            ),
            habitId?.toString() ?: UUID.randomUUID().toString(),
            creationDate ?: Calendar.getInstance()
        )
    }
}