package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.Strings.HABIT_EDIT_CREATE
import com.example.habittracker.Strings.HABIT_EDIT_POSITION
import kotlinx.android.synthetic.main.activity_habit.*
import kotlin.random.Random

class HabitActivity: AppCompatActivity() {
    private var habitToEdit: Habit? = null
    private var habitPosition: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit)

        arrayOf(nameTextField, descriptionTextField, countTextField, periodTextField).onTextChange(save_habit_button)

        habitToEdit = intent.extras?.getParcelable(HABIT_EDIT_CREATE)
            ?: savedInstanceState?.getParcelable(HABIT_EDIT_CREATE)
        habitPosition = intent.extras?.getInt(HABIT_EDIT_POSITION)
            ?: savedInstanceState?.getInt(HABIT_EDIT_POSITION)

        if (habitToEdit != null)
            initializeHabitView()

        save_habit_button.setOnClickListener{
            val newHabit = getNewHabit()
            val intent = if (habitPosition == null)
                Intent().putExtra(HABIT_EDIT_CREATE, newHabit)
            else
                Intent().putExtra(HABIT_EDIT_CREATE, newHabit)
                    .putExtra(HABIT_EDIT_POSITION, habitPosition as Int)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(HABIT_EDIT_CREATE, getNewHabit())
        if (habitPosition != null)
            outState.putInt(HABIT_EDIT_POSITION, habitPosition as Int)
    }

    private fun initializeHabitView(){
        val habit = habitToEdit as Habit
        nameTextField.setText(habit.name)
        descriptionTextField.setText(habit.description)
        habitRadioGroup.check(if (habit.type == HabitType.Good) habitTypeRadioButtonGood.id else habitTypeRadioButtonBad.id )
        prioritySpinner.setSelection(habit.priority-1)
        countTextField.setText(habit.count.toString())
        periodTextField.setText(habit.period.toString())
    }

    private fun getNewHabit(): Habit{
        val name = nameTextField.text.toString()
        val description = descriptionTextField.text.toString()
        val type = HabitType.valueOf(findViewById<RadioButton>(habitRadioGroup.checkedRadioButtonId).text.toString())
        val priority = prioritySpinner.selectedItem.toString().toInt()
        val count = countTextField.text.toString().toIntOrNull() ?: 0
        val period = periodTextField.text.toString().toIntOrNull() ?: 0
        val color = habitToEdit?.color
            ?: Color.argb(255,
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256))
        return Habit(name, description, priority, type, count, period, color)
    }
}