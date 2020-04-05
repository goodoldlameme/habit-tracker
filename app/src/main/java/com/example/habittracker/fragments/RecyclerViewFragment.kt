package com.example.habittracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.models.Habit
import com.example.habittracker.R
import com.example.habittracker.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.recycler_view_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewFragment : Fragment() {
    private var recyclerAdapter: RecyclerAdapter? = null
    private var habits: ArrayList<Habit> = ArrayList()
    private var callback: RecyclerViewCallback? = null

    interface RecyclerViewCallback {
        fun onEditClickListener(habitId: UUID)
    }

    companion object{
        private const val HABITS_ARG = "HABITS_ARG"

        fun newInstance(habits: ArrayList<Habit>) : RecyclerViewFragment {
            val args = Bundle()
            args.putParcelableArrayList(HABITS_ARG, habits)
            val newFragment =
                RecyclerViewFragment()
            newFragment.arguments = args
            return newFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(HABITS_ARG, habits)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as RecyclerViewCallback
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
        return inflater.inflate(R.layout.recycler_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            callback?.let { callback ->
                recyclerAdapter =
                    RecyclerAdapter(habits)
                layoutManager = LinearLayoutManager(activity)
                recyclerAdapter?.setOnViewHolderClickListener { habit, _ ->
                    callback.onEditClickListener(habit.id)
                }
                adapter = recyclerAdapter
            }
        }
    }
}
