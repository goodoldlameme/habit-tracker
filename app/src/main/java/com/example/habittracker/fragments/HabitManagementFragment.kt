package com.example.habittracker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.App
import com.example.habittracker.model.ManagementViewModel
import com.example.habittracker.R
import com.example.habittracker.model.HabitType
import com.example.habittracker.common.onTextChangeButtonEnabler
import com.example.habittracker.repository.HabitsProvider
import com.example.habittracker.repository.RoomHabitsProvider
import kotlinx.android.synthetic.main.habit_fragment.*
import java.util.*
import javax.inject.Inject

class HabitManagementFragment: Fragment() {
    private var callback: HabitManagementCallback? = null
    private var habitUUID: UUID? = null
    @Inject lateinit var habitsProvider: HabitsProvider
    private lateinit var viewModel: ManagementViewModel


    companion object{
        private const val HABIT_UUID_ARGS = "HABIT_UUID_ARGS"

        fun newInstance(habitUUID: UUID) : HabitManagementFragment {
            val args = Bundle()
            args.putSerializable(HABIT_UUID_ARGS, habitUUID)
            val newFragment =
                HabitManagementFragment()
            newFragment.arguments = args
            return newFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.inject(this)
        habitUUID = (savedInstanceState?.getSerializable(HABIT_UUID_ARGS)
            ?: arguments?.getSerializable(HABIT_UUID_ARGS)) as? UUID

        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ManagementViewModel(
                    habitsProvider,
                    habitUUID
                ) as T
            }
        }).get(ManagementViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.editHabit.value?.let{ viewModel.setHabit(it)}
        outState.putSerializable(HABIT_UUID_ARGS, habitUUID)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as HabitManagementCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.habit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFieldListeners()

        viewModel.habit.observe(this, androidx.lifecycle.Observer { habit ->
            if (habit.name != null) nameTextField.setText(habit.name)
            if (habit.description != null) descriptionTextField.setText(habit.description)
            habitRadioGroup.check(if (habit.type == HabitType.Good) habitTypeRadioButtonGood.id else habitTypeRadioButtonBad.id )
            prioritySpinner.setSelection(habit.priority as Int - 1)
            if (habit.count != null) countTextField.setText(habit.count.toString())
            if (habit.period != null) periodTextField.setText(habit.period.toString())
        })

        save_habit_button.setOnClickListener{
            viewModel.editHabit.value?.let{ viewModel.setHabit(it)}
            viewModel.updateHabit()
            callback?.onSaveClickListener()
        }
    }

    private fun setFieldListeners(){
        arrayOf(nameTextField, descriptionTextField, countTextField, periodTextField)
            .onTextChangeButtonEnabler(save_habit_button)

        nameTextField.doAfterTextChanged { text: Editable? ->
            val newName = text?.toString()
            viewModel.editHabit.value?.let{
                it.name = newName
                viewModel.setEditHabit(it)
            }
        }
        descriptionTextField.doAfterTextChanged{ text: Editable? ->
            val newDescription = text?.toString()
            viewModel.editHabit.value?.let{
                it.description = newDescription
                viewModel.setEditHabit(it)
            }
        }
        countTextField.doAfterTextChanged{ text: Editable? ->
            val newCount = text?.toString()?.toIntOrNull()
            viewModel.editHabit.value?.let{
                it.count = newCount
                viewModel.setEditHabit(it)
            }
        }
        periodTextField.doAfterTextChanged{ text: Editable? ->
            val newPeriod = text?.toString()?.toIntOrNull()
            viewModel.editHabit.value?.let{
                it.period = newPeriod
                viewModel.setEditHabit(it)
            }
        }
        habitRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newHabitType = HabitType.valueOf(view?.findViewById<RadioButton>(checkedId)?.text.toString())
            viewModel.editHabit.value?.let{
                it.type = newHabitType
                viewModel.setEditHabit(it)
            }
        }
        prioritySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                val newPriority = selectedItem.toInt()
                viewModel.editHabit.value?.let{
                    it.priority = newPriority
                    viewModel.setEditHabit(it)
                }
            }
        }
    }

    interface HabitManagementCallback{
        fun onSaveClickListener()
    }
}