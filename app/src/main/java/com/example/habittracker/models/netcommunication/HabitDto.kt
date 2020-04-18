package com.example.habittracker.models.netcommunication

import com.example.habittracker.models.HabitType
import com.example.habittracker.models.Priority
import com.example.habittracker.models.database.HabitEntity
import com.example.habittracker.models.database.typeconverters.CalendarTypeConverters
import com.example.habittracker.models.viewmodels.EditHabit
import java.util.*

data class HabitDto(val color: Int,
                    val count: Int,
                    val date: Long,
                    val description: String,
                    val frequency: Int,
                    val priority: Priority,
                    val title: String,
                    val type: HabitType,
                    val uid: String? = null
){
    fun toHabitEntity(habitId: String? = null): HabitEntity{
        return HabitEntity(
            title,
            description,
            priority,
            type,
            count,
            frequency,
            color,
            uid ?: habitId.toString(),
            CalendarTypeConverters().toCalendar(date.toString())
        )
    }
}

