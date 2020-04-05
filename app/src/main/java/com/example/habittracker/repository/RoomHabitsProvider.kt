package com.example.habittracker.repository

import androidx.lifecycle.LiveData
import com.example.habittracker.App
import com.example.habittracker.database.HabitEntity
import java.util.*

object RoomHabitsProvider: HabitsProvider{
    private val habitsDatabase = App.database

    override fun getHabit(habitID: UUID?): LiveData<HabitEntity> {
        return  habitsDatabase.getHabitsDao().getHabitById(habitID.toString())
    }

    override fun loadHabits(): LiveData<List<HabitEntity>> =
        habitsDatabase.getHabitsDao().getAll()

    override suspend fun addOrUpdateHabit(habitEntity: HabitEntity) {
        habitsDatabase.getHabitsDao().insertOrReplaceHabit(habitEntity)
    }

    override suspend fun deleteHabit(habitEntity: HabitEntity) {
        habitsDatabase.getHabitsDao().deleteHabit(habitEntity)
    }
}