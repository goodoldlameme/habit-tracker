package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.events.HabitChangedEventHandler
import com.example.habittracker.repository.HabitsProvider
import java.util.*

class ManagementViewModel(private val habitsProvider: HabitsProvider, private val habitId: UUID?): ViewModel() {
    private val mutableHabit: MutableLiveData<EditHabit> = MutableLiveData()
    val habit: LiveData<EditHabit> = mutableHabit

    private val mutableEditHabit: MutableLiveData<EditHabit> = MutableLiveData()
    val editHabit: LiveData<EditHabit> = mutableEditHabit

    init {
        load()
    }

    private fun load(){
        if (habitId != null){
            habitsProvider.getHabit(habitId).value?.let { habit ->
                mutableHabit.value = EditHabit.fromHabitEntity(habit)
                mutableEditHabit.value = EditHabit.fromHabitEntity(habit)
            }
        }
        else {
            val editHabit = EditHabit()
            editHabit.type = HabitType.Good
            editHabit.priority = 1
            mutableEditHabit.value = editHabit
        }
    }

    fun setEditHabit(editHabit: EditHabit){
        mutableEditHabit.value = editHabit
    }

    fun setHabit(habit: EditHabit){
        mutableHabit.value = habit
    }

    fun updateHabit(){
        mutableHabit.value?.let {h ->
            habitsProvider.addOrUpdateHabit(h.toHabitEntity(habitId))
//            HabitChangedEventHandler.handleHabitChangedEvent()
        }
    }
}