package com.example.habittracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.model.Habit
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface HabitsDao {
    @Query("SELECT * FROM HabitEntity")
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE id LIKE :habitId")
    fun getHabitById(habitId: String): LiveData<HabitEntity>

    @Update
    fun updateHabit(habit: HabitEntity)

    @Insert
    fun insertHabit(habit: HabitEntity)
}