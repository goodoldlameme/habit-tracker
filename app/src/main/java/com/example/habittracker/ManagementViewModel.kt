package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
            habitsProvider.getHabit(habitId)?.let { habit ->
                mutableHabit.value = EditHabit.fromHabit(habit)
                mutableEditHabit.value = EditHabit.fromHabit(habit)
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
            habitsProvider.addOrUpdateHabits(h.toHabit(habitId))
            HabitChangedEventHandler.handleHabitChangedEvent()
        }
    }
}