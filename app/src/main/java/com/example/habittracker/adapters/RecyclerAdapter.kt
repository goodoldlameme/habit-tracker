package com.example.habittracker.adapters

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.common.Strings
import com.example.habittracker.common.inflate
import com.example.habittracker.models.Habit
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapter(private val habits: ArrayList<Habit>) : RecyclerView.Adapter<RecyclerAdapter.HabitHolder>()  {
    private lateinit var onViewHolderClickListener: (Habit, Int) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return HabitHolder(
            inflatedView,
            onViewHolderClickListener
        )
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitHolder, position: Int) {
        val itemHabit = habits[position]
        holder.bindHabit(itemHabit)
    }

    fun setOnViewHolderClickListener(onViewHolderClickListener: (Habit, Int) -> Unit){
        this.onViewHolderClickListener = onViewHolderClickListener
    }

    class HabitHolder(private var view: View, private val onViewHolderClickListener: (Habit, Int) -> Unit)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var habit: Habit? = null

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (habit != null)
                onViewHolderClickListener(habit as Habit, adapterPosition)
        }

        fun bindHabit(habit: Habit) {
            this.habit = habit
            view.itemName.text = habit.name
            view.itemDescription.text = habit.description
            view.itemColor.text = habit.priority.toString()
            view.itemPeriod.text = Strings.get(
                R.string.period_message,
                habit.period
            )
            view.itemCount.text = Strings.get(
                R.string.count_message,
                habit.count
            )
            val background = ShapeDrawable()
            background.shape = OvalShape()
            background.paint.color = habit.color
            view.itemColor.background = background
            view.itemType.text = habit.type.toString()
            view.itemCreation.text = Strings.get(
                R.string.creation_message,
                habit.creationDate.get(Calendar.DAY_OF_MONTH),
                habit.creationDate.get(Calendar.MONTH),
                habit.creationDate.get(Calendar.YEAR)
            )
        }
    }
}
