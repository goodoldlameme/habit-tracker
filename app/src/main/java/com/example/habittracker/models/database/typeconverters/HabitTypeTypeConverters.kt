package com.example.habittracker.models.database.typeconverters

import androidx.room.TypeConverter
import com.example.habittracker.models.HabitType

class HabitTypeTypeConverters{
    @TypeConverter
    fun fromHabitType(habitType: HabitType): String = habitType.name

    @TypeConverter
    fun toHabitType(string: String): HabitType = HabitType.valueOf(string)
}