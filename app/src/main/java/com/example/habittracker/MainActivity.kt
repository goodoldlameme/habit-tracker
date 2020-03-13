package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private val habits = ArrayList<Habit>()

    companion object {
        const val START_CREATE_HABIT_ACTIVITY_REQUEST_CODE = 0
        const val START_EDIT_HABIT_ACTIVITY_REQUEST_CODE = 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(habits)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivityForResult(Intent(this, HabitActivity::class.java), START_CREATE_HABIT_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val habit = data?.extras?.getParcelable<Habit>(HabitActivity.HABIT_CREATE)
        when(requestCode){
            START_CREATE_HABIT_ACTIVITY_REQUEST_CODE -> handleHabitCreation(habit)
            START_EDIT_HABIT_ACTIVITY_REQUEST_CODE -> handleHabitEditing(habit)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleHabitCreation(habit: Habit?){
        if (habit != null) {
            habits.add(habit)
            adapter.notifyDataSetChanged()
        }
    }

    private fun handleHabitEditing(habit: Habit?){
        if (habit != null) {
            habits.add(habit)
            adapter.notifyDataSetChanged()
        }
    }
}
