package com.example.habittracker.models.database.typeconverters

import androidx.room.TypeConverter
import com.example.habittracker.models.Priority
import java.util.*

class CalendarTypeConverters{
    @TypeConverter
    fun fromCalendar(calendar: Calendar): String = calendar.timeInMillis.toString()

    @TypeConverter
    fun toCalendar(string: String): Calendar {
        val result = Calendar.getInstance()
        result.timeInMillis = string.toLong()
        return result
    }
}

class PriorityTypeConverters{
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(string: String): Priority = Priority.valueOf(string)
}