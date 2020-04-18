package com.example.habittracker.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.habittracker.App
import com.example.habittracker.models.database.HabitEntity
import com.example.habittracker.models.netcommunication.HabitDto
import com.example.habittracker.models.netcommunication.HabitId
import com.example.habittracker.netcommunication.HabitsRemoteService
import java.lang.Exception
import java.util.*

object RoomHabitsProvider: HabitsProvider{
    private val habitsDatabase = App.database
    private val remoteService: HabitsRemoteService = HabitsRemoteService.getInstance()

    const val LOG_TAG = "RoomHabitsProvider"

    override fun getHabit(habitID: UUID?): LiveData<HabitEntity> {
        return habitsDatabase.getHabitsDao().getHabitById(habitID.toString())
    }

    override fun getHabits(): LiveData<List<HabitEntity>> =
        habitsDatabase.getHabitsDao().getAll()

    override suspend fun loadHabitsFromRemote(){
        val response = HabitsRemoteService.retry{ remoteService.getHabits()}

        response?.let { response ->
            response.body()?.let{ habitDtos ->
                val habits = habitDtos.map { it.toHabitEntity() }
                addOrUpdateToLocal(habits)
            }
        }
    }

    private suspend fun addOrUpdateToLocal(habits: List<HabitEntity>){
        habitsDatabase.getHabitsDao().insertOrReplaceHabits(habits)
    }

    override suspend fun addOrUpdateHabit(habitDto: HabitDto) {
        val response = HabitsRemoteService.retry { remoteService.addOrUpdateHabit(habitDto) }


        response?.let{response ->
            response.body()?.let{ id ->
                val habitEntity = habitDto.toHabitEntity(id.uid)
                habitsDatabase.getHabitsDao().insertOrReplaceHabit(habitEntity)
            }
        }
    }

    override suspend fun deleteHabit(habitEntity: HabitEntity): Boolean {
        val response = HabitsRemoteService.retry{ remoteService.deleteHabit(HabitId(habitEntity.id))}

        response?.let{
            habitsDatabase.getHabitsDao().deleteHabit(habitEntity)
            return true
        }
        return false
    }
}