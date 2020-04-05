package com.example.habittracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.habittracker.models.HabitType
import java.util.*

@Entity
@TypeConverters(CalendarTypeConverters::class, HabitTypeTypeConverters::class)
data class HabitEntity(val name: String,
                       val description: String,
                       val priority: Int,
                       val type: HabitType,
                       val count: Int,
                       val period: Int,
                       val color: Int,
                       @PrimaryKey
                       val id: String,
                       val creationDate: Calendar) {
}

class CalendarTypeConverters{
    @TypeConverter
    fun fromCalendar(calendar: Calendar): String = calendar.timeInMillis.toString()

    @TypeConverter
    fun toCalendar(string: String): Calendar{
        val result = Calendar.getInstance()
        result.timeInMillis = string.toLong()
        return result
    }
}

class HabitTypeTypeConverters{
    @TypeConverter
    fun fromHabitType(habitType: HabitType): String = habitType.name

    @TypeConverter
    fun toHabitType(string: String): HabitType = HabitType.valueOf(string)
}