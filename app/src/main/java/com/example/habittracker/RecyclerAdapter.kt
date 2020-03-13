package com.example.habittracker

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.Strings.HABIT_EDIT_CREATE
import com.example.habittracker.Strings.HABIT_EDIT_POSITION
import com.example.habittracker.Strings.START_EDIT_HABIT_ACTIVITY_REQUEST_CODE
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*


class RecyclerAdapter(private val habits: ArrayList<Habit>) : RecyclerView.Adapter<RecyclerAdapter.HabitHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return HabitHolder(inflatedView)
    }

    override fun getItemCount(): Int = habits.size

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: HabitHolder, position: Int) {
        val itemHabit = habits[position]
        holder.bindHabit(itemHabit)
    }

    class HabitHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var habit: Habit? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context as MainActivity
            val editHabitIntent = Intent(context, HabitActivity::class.java)
            editHabitIntent
                .putExtra(HABIT_EDIT_CREATE, habit)
                .putExtra(HABIT_EDIT_POSITION, adapterPosition)
            context.startActivityForResult(editHabitIntent, START_EDIT_HABIT_ACTIVITY_REQUEST_CODE)
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun bindHabit(habit: Habit) {
            this.habit = habit
            view.itemName.text = habit.name
            view.itemDescription.text = habit.description
            view.itemType.text = habit.type.name
            view.itemColor.text = habit.priority.toString()
            view.itemPeriod.text = Strings.get(R.string.period_message, habit.period)
            val background = ShapeDrawable()
            background.shape = OvalShape()
            background.paint.color = habit.color
            view.itemColor.background = background
        }
    }
}
