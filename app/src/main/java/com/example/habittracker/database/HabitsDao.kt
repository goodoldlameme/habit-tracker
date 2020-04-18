package com.example.habittracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.habittracker.models.database.HabitEntity

@Dao
interface HabitsDao {
    @Query("SELECT * FROM HabitEntity")
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE id = :habitId LIMIT 1")
    fun getHabitById(habitId: String): LiveData<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceHabits(habits: List<HabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}