package com.example.habittracker.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.*
import com.example.habittracker.common.onTextChangeButtonEnabler
import com.example.habittracker.models.viewmodels.EditHabit
import com.example.habittracker.models.HabitType
import com.example.habittracker.models.Priority
import com.example.habittracker.models.viewmodels.ManagementViewModel
import com.example.habittracker.repository.RoomHabitsProvider
import kotlinx.android.synthetic.main.habit_fragment.*
import java.lang.reflect.Array
import java.util.*

class HabitManagementFragment: Fragment() {
    private var callback: HabitManagementCallback? = null
    private var habitUUID: UUID? = null
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
        habitUUID = (savedInstanceState?.getSerializable(HABIT_UUID_ARGS)
            ?: arguments?.getSerializable(HABIT_UUID_ARGS)) as? UUID

        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ManagementViewModel(
                    RoomHabitsProvider,
                    habitUUID
                ) as T
            }
        }).get(ManagementViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
        context?.let{context ->
            prioritySpinner.adapter = ArrayAdapter(context, R.layout.spinner_item, Priority.values().map{it.name})}
        setFieldListeners()
        setViewModelObservers()

        save_habit_button.setOnClickListener{
            viewModel.updateHabit()
            callback?.onSaveClickListener()
        }
    }

    private fun setViewModelObservers(){
        viewModel.loadHabitsFromRemote()
        viewModel.habitSource.observe(this, androidx.lifecycle.Observer {  habit -> habit?.let{viewModel.setFromSource(it)} ?: viewModel.setWithOutSource()})
        viewModel.editHabit.observe(this, androidx.lifecycle.Observer { habit -> setHabitViews(habit)})
    }

    private fun setHabitViews(habit: EditHabit){
        shouldChange(habit.name, nameTextField)?.let{ newField -> nameTextField.setText(newField)}
        shouldChange(habit.description, descriptionTextField)?.let{ newField -> descriptionTextField.setText(newField)}
        shouldChange(habit.count?.toString(), countTextField)?.let{ newField -> countTextField.setText(newField)}
        shouldChange(habit.period?.toString(), periodTextField)?.let{ newField -> periodTextField.setText(newField)}
        if (habit.type != radioButtonIdToHabitType(habitRadioGroup.checkedRadioButtonId)) habitRadioGroup.check(habitTypeToRadioButtonId(habit.type))
        if (habit.priority?.name != prioritySpinner.selectedItem)prioritySpinner.setSelection(habit.priority!!.ordinal)
    }

    private fun shouldChange(newField: String?, editText: EditText): String? =
        if (newField != null && newField != editText.text.toString()) newField else null

    private fun radioButtonIdToHabitType(radioButtonId: Int): HabitType =
        if (radioButtonId == habitTypeRadioButtonGood.id) HabitType.Good  else HabitType.Bad

    private fun habitTypeToRadioButtonId(habitType: HabitType?): Int =
        if (habitType == HabitType.Good) habitTypeRadioButtonGood.id else habitTypeRadioButtonBad.id

    private fun setFieldListeners(){
        arrayOf(nameTextField, descriptionTextField, countTextField, periodTextField)
            .onTextChangeButtonEnabler(save_habit_button)

        nameTextField.doAfterTextChanged { text: Editable? ->
            val newName = text?.toString()
            viewModel.setEditHabitField { habit -> habit?.let { it.name = newName }}
        }
        descriptionTextField.doAfterTextChanged{ text: Editable? ->
            val newDescription = text?.toString()
            viewModel.setEditHabitField { habit -> habit?.let { it.description = newDescription } }

        }
        countTextField.doAfterTextChanged{ text: Editable? ->
            val newCount = text?.toString()?.toIntOrNull()
            viewModel.setEditHabitField { habit -> habit?.let { it.count = newCount } }
        }
        periodTextField.doAfterTextChanged{ text: Editable? ->
            val newPeriod = text?.toString()?.toIntOrNull()
            viewModel.setEditHabitField { habit -> habit?.let { it.period = newPeriod } }
        }
        habitRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newHabitType = HabitType.valueOf(view?.findViewById<RadioButton>(checkedId)?.text.toString())
            viewModel.setEditHabitField { habit -> habit?.let { it.type = newHabitType } }
        }
        prioritySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                viewModel.setEditHabitField { habit -> habit?.let { it.priority = Priority.valueOf(selectedItem) } }
            }
        }
    }

    interface HabitManagementCallback{
        fun onSaveClickListener()
    }
}