package com.example.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.view_pager_layout.*

class ViewPagerFragment: Fragment() {
    private var habitsViewPagerAdapter: HabitsViewPagerAdapter? = null
    private var habits: ArrayList<Habit> = ArrayList()

    companion object{
        private const val HABITS_ARG = "HABITS_ARG"

        fun newInstance(habits: ArrayList<Habit>) : ViewPagerFragment{
            val args = Bundle()
            args.putParcelableArrayList(HABITS_ARG, habits)
            val newFragment = ViewPagerFragment()
            newFragment.arguments = args
            return newFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(HABITS_ARG, habits)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habits = arguments?.getParcelableArrayList(HABITS_ARG)
            ?: savedInstanceState?.getParcelableArrayList(HABITS_ARG) ?: ArrayList()
    }

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
            habitsViewPagerAdapter = HabitsViewPagerAdapter(habits, childFragmentManager, this.lifecycle)
            habitsViewPager.adapter = habitsViewPagerAdapter
            TabLayoutMediator(habitsTabLayout, habitsViewPager){ tab, position ->
                tab.text = if (position == 0) "Good" else "Bad"
            }.attach()
        }
    }
}