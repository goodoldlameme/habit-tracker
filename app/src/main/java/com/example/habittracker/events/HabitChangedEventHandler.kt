package com.example.habittracker.events

object HabitChangedEventHandler {
    private val onHabitsChangeListeners: ArrayList<HabitChangeListener> = ArrayList()

    fun setOnHabitChangeListener(habitChangeListener: HabitChangeListener){
        onHabitsChangeListeners.add(habitChangeListener)
    }

    fun handleHabitChangedEvent(){
        onHabitsChangeListeners.forEach { habitChangeListener: HabitChangeListener -> habitChangeListener.onHabitChanged() }
    }
}