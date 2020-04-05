package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.habittracker.models.Habit
import com.example.habittracker.adapters.HabitsViewPagerAdapter
import com.example.habittracker.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.view_pager_layout.*

class ViewPagerFragment: Fragment() {
    private var habitsViewPagerAdapter: HabitsViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_pager_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{ _ ->
            habitsViewPagerAdapter =
                HabitsViewPagerAdapter(
                    childFragmentManager,
                    this.lifecycle
                )
            habitsViewPager.adapter = habitsViewPagerAdapter
            TabLayoutMediator(habitsTabLayout, habitsViewPager){ tab, position ->
                tab.text = if (position == 0) "Good" else "Bad"
            }.attach()
        }
    }
}