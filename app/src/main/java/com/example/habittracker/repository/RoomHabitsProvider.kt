package com.example.habittracker.repository

import androidx.lifecycle.LiveData
import com.example.habittracker.App
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.model.Habit
import dagger.Binds
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class RoomHabitsProvider @Inject constructor(private val habitsDatabase: HabitsDatabase): HabitsProvider{

    override fun getHabit(habitID: UUID): LiveData<HabitEntity> =
        habitsDatabase.getHabitsDao().getHabitById(habitID.toString())

    override fun loadHabits(): LiveData<List<HabitEntity>> =
        habitsDatabase.getHabitsDao().getAll()

    override fun addOrUpdateHabit(habit: HabitEntity) {
        if (habitsDatabase.getHabitsDao().getHabitById(habit.id).value != null)
            habitsDatabase.getHabitsDao().updateHabit(habit)
        else
            habitsDatabase.getHabitsDao().insertHabit(habit)
    }

}