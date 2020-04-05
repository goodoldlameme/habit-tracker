package com.example.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.fragments.RecyclerViewFragment
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitType


class HabitsViewPagerAdapter(private val habits: ArrayList<Habit>, private val fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { RecyclerViewFragment.newInstance(ArrayList(habits.filter { habit -> habit.type == HabitType.Good}))}
            else -> {
                RecyclerViewFragment.newInstance(ArrayList(habits.filter { habit -> habit.type == HabitType.Bad}))}
        }
    }
}