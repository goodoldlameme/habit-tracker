package com.example.habittracker

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class HabitsViewPagerAdapter(private val habits: ArrayList<Habit>, private val fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { RecyclerViewFragment.newInstance(ArrayList(habits.filter { habit -> habit.type == HabitType.Good}))}
            else -> {RecyclerViewFragment.newInstance(ArrayList(habits.filter { habit -> habit.type == HabitType.Bad}))}
        }
    }
}