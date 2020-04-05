package com.example.habittracker.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.repository.HabitsProvider
import com.example.habittracker.models.ListViewSettings
import com.example.habittracker.models.Habit
import kotlinx.coroutines.launch

class ListHabitsViewModel(private val habitsProvider: HabitsProvider) : ViewModel()
{
    val habitsSource: LiveData<List<HabitEntity>> by lazy {
        habitsProvider.loadHabits()
    }

    private val mutableViewSettings: MutableLiveData<ListViewSettings> = MutableLiveData()
    val viewSettings: LiveData<ListViewSettings> = mutableViewSettings

    private val mutableFilteredHabits: MutableLiveData<List<Habit>> = MutableLiveData()
    val filteredHabits: LiveData<List<Habit>> = mutableFilteredHabits

    private val mutableHabits: MutableLiveData<List<HabitEntity>> = MutableLiveData()

    fun updateViewSettings(selector: (ListViewSettings?) -> Unit){
        if (mutableViewSettings.value == null)
            mutableViewSettings.value = ListViewSettings()
        selector(mutableViewSettings.value)
        mutableHabits.value?.let{ updateFilteredHabits(it) }
    }

    fun clearViewSettings(){
        mutableViewSettings.value = null
    }

    fun updateFilteredHabits(habits: List<HabitEntity>){
        mutableHabits.value = habits

        val habitEntities: List<HabitEntity> = mutableViewSettings.value?.let{ settings -> getFilteredHabits(habits, settings) } ?: habits

        mutableFilteredHabits.value = habitEntities.map { habitEntity -> Habit.fromHabitEntity(habitEntity)}
    }

    fun deleteHabit(habitToDelete: HabitEntity){
        viewModelScope.launch { habitsProvider.deleteHabit(habitToDelete) }
    }

    private fun getFilteredHabits(habits: List<HabitEntity>, settings: ListViewSettings): List<HabitEntity>{
        val res = settings.searchByName?.let{ searchByName ->
            if (searchByName.isEmpty())
                habits
            else
                habits.filter { h -> h.name.contains(searchByName) }
        } ?: habits

        return settings.sortByDescending?.let{ sortByDescending -> if (sortByDescending) res.sortedByDescending { h -> h.creationDate } else res.sortedBy{ h -> h.creationDate } } ?: res
    }
}