package com.example.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.models.HabitType


class HabitsViewPagerAdapter(private val fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RecyclerViewFragment.newInstance(HabitType.Good)
            else -> RecyclerViewFragment.newInstance(HabitType.Bad)
        }
    }
}