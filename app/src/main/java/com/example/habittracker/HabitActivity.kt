package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_habit.*
import kotlin.random.Random

class HabitActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit)

        val nameField = nameTextField
        val descriptionField = descriptionTextField
        val habitTypeField = habitRadioGroup
        val priorityField = prioritySpinner
        val habitCountField = countTextField
        val habitPeriodField = periodTextField

        arrayOf(nameField, descriptionField, habitCountField, habitPeriodField).onTextChange(save_habit_button)

        save_habit_button.setOnClickListener{
            tuneListener(nameField, descriptionField, habitTypeField, priorityField, habitCountField, habitPeriodField)
        }
    }

    private fun tuneListener(
        nameField: EditText,
        descriptionField: EditText,
        habitTypeField: RadioGroup,
        priorityField: Spinner,
        habitCountField: EditText,
        habitPeriodField: EditText){
        val name = nameField.text.toString()
        val description = descriptionField.text.toString()
        val type = HabitType.valueOf(findViewById<RadioButton>(habitTypeField.checkedRadioButtonId).text.toString())
        val priority = priorityField.selectedItem.toString().toInt()
        val count = habitCountField.text.toString().toInt()
        val period = habitPeriodField.text.toString().toInt()
        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val habit = Habit(name, description, priority, type, count, period, color)
        val intent = Intent().apply { putExtra(HABIT_CREATE, habit) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val HABIT_CREATE = "HABIT_CREATE"
    }
}