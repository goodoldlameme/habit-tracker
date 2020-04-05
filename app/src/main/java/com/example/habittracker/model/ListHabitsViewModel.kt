package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.database.HabitEntity
import com.example.habittracker.events.HabitChangeListener
import com.example.habittracker.repository.HabitsProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListHabitsViewModel @Inject constructor(private val habitsProvider: HabitsProvider) : ViewModel(),
    HabitChangeListener {
    private var mutableHabits: LiveData<List<HabitEntity>> = MutableLiveData()
    val habits: LiveData<List<HabitEntity>> = mutableHabits

    private val mutableViewSettings: MutableLiveData<ListViewSettings> = MutableLiveData()
    val viewSettings: LiveData<ListViewSettings> = mutableViewSettings

    init{
        loadHabits()
    }

    fun updateViewSettings(listViewSettings: ListViewSettings?){
        mutableViewSettings.value = listViewSettings
    }

    fun loadHabits(){
        mutableHabits = habitsProvider.loadHabits()
    }

    fun getFilteredHabits(): ArrayList<Habit>{
        val habitEntities: List<HabitEntity>? = mutableHabits.value?.let{ habitsValue ->
            mutableViewSettings.value?.let{ settings -> getFilteredHabits(habitsValue, settings) } ?: habitsValue }

        return ArrayList(habitEntities?.map { habitEntity -> Habit.fromHabitEntity(habitEntity)} ?: listOf())
    }

    private fun getFilteredHabits(habits: List<HabitEntity>, settings: ListViewSettings): List<HabitEntity>{
        val res = settings.searchByName?.let{ searchByName -> habits.filter { h -> h.name.contains(searchByName) } } ?: habits
        settings.sortByDescending?.let{ sortByDescending -> if (sortByDescending) res.sortedByDescending { h -> h.creationDate } else res.sortedBy{ h -> h.creationDate } }
        return res
    }

    override fun onHabitChanged() {
        loadHabits()
    }
}