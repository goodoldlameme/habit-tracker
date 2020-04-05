package com.example.habittracker.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.events.HabitChangeListener
import com.example.habittracker.repository.HabitsProvider
import com.example.habittracker.models.ListViewSettings
import com.example.habittracker.models.Habit

class ListHabitsViewModel(private val habitsProvider: HabitsProvider) : ViewModel(),
    HabitChangeListener {
    private val mutableHabits: MutableLiveData<ArrayList<Habit>> = MutableLiveData()
    val habits: LiveData<ArrayList<Habit>> = mutableHabits

    private val mutableViewSettings: MutableLiveData<ListViewSettings> = MutableLiveData()
    val viewSettings: LiveData<ListViewSettings> = mutableViewSettings

    init{
        loadHabits()
    }

    fun updateViewSettings(listViewSettings: ListViewSettings?){
        mutableViewSettings.value = listViewSettings
    }

    fun loadHabits(){
        mutableHabits.value = habitsProvider.loadHabits()
    }

    fun getFilteredHabits(): ArrayList<Habit>?{
        return mutableHabits.value?.let{ habitsValue ->
            return mutableViewSettings.value?.let{ settings -> getFilteredHabits(habitsValue, settings) } ?: habitsValue }
    }

    private fun getFilteredHabits(habits: ArrayList<Habit>, settings: ListViewSettings): ArrayList<Habit>{
        val res = ArrayList(settings.searchByName?.let{ searchByName -> habits.filter { h -> h.name.contains(searchByName) } } ?: habits)
        settings.sortByDescending?.let{ sortByDescending -> if (sortByDescending) res.sortByDescending { h -> h.creationDate } else res.sortBy{ h -> h.creationDate } }
        return res
    }

    override fun onHabitChanged() {
        loadHabits()
    }
}