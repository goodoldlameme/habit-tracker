package com.example.habittracker.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.repository.HabitsProvider
import com.example.habittracker.models.EditHabit
import com.example.habittracker.models.HabitType
import kotlinx.coroutines.launch
import java.util.*

class ManagementViewModel(private val habitsProvider: HabitsProvider, private val habitId: UUID?): ViewModel() {

    val habitSource: LiveData<HabitEntity> by lazy {
        habitsProvider.getHabit(habitId)
    }

    private val mutableEditHabit: MutableLiveData<EditHabit> = MutableLiveData()
    val editHabit: LiveData<EditHabit> = mutableEditHabit

    fun setWithOutSource() {
        if (mutableEditHabit.value == null){
            val editHabit = EditHabit()
            editHabit.type = HabitType.Good
            editHabit.priority = 1
            mutableEditHabit.value = editHabit
        }
    }

    fun setFromSource(habit: HabitEntity){
        if (mutableEditHabit.value == null) {
            mutableEditHabit.value = EditHabit.fromHabitEntity(habit)
        }
    }

    fun setEditHabitField(selector: (EditHabit?) -> Unit){
        selector(mutableEditHabit.value)
    }

    fun updateHabit(){
        viewModelScope.launch { mutableEditHabit.value?.let {h ->
            habitsProvider.addOrUpdateHabit(h.toHabitEntity(habitId)) }
        }
    }
}