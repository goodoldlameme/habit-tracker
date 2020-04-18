package com.example.habittracker.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.habittracker.models.HabitType
import com.example.habittracker.models.Priority
import com.example.habittracker.models.database.typeconverters.CalendarTypeConverters
import com.example.habittracker.models.database.typeconverters.HabitTypeTypeConverters
import com.example.habittracker.models.database.typeconverters.PriorityTypeConverters
import com.example.habittracker.models.netcommunication.HabitDto
import java.util.*

@Entity
@TypeConverters(CalendarTypeConverters::class, HabitTypeTypeConverters::class, PriorityTypeConverters::class)
data class HabitEntity(val name: String,
                       val description: String,
                       val priority: Priority,
                       val type: HabitType,
                       val count: Int,
                       val period: Int,
                       val color: Int,
                       @PrimaryKey
                       val id: String,
                       val creationDate: Calendar) {
}

