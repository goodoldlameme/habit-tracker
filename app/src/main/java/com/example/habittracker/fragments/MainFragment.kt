package com.example.habittracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.*
import com.example.habittracker.events.HabitChangedEventHandler
import com.example.habittracker.models.Habit
import com.example.habittracker.models.ListViewSettings
import com.example.habittracker.models.viewmodels.ListHabitsViewModel
import com.example.habittracker.repository.LocalHabitsProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(){
    private var callback: MainFragmentCallback? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var viewModel: ListHabitsViewModel

    companion object{
        private val BOTTOM_SHEET_STATE = "BOTTOM_SHEET_STATE"
    }

    interface MainFragmentCallback{
        fun setFabOnClickListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as MainFragmentCallback
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_SHEET_STATE, bottomSheetBehavior?.state ?: BottomSheetBehavior.STATE_COLLAPSED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListHabitsViewModel(
                    LocalHabitsProvider
                ) as T
            }
        }).get(ListHabitsViewModel::class.java)

        HabitChangedEventHandler.setOnHabitChangeListener(
            viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.let{ callback -> fab.setOnClickListener { callback.setFabOnClickListener() }}
        setupBottomSheet(savedInstanceState?.getInt(BOTTOM_SHEET_STATE) ?: BottomSheetBehavior.STATE_COLLAPSED)
        setupFilters()
        setUpViewModel()
    }

    private fun setUpViewModel(){
        viewModel.habits.observe(this, Observer { habits ->
            setFragment(bottomSheetBehavior?.state
                ?: BottomSheetBehavior.STATE_COLLAPSED, viewModel.getFilteredHabits() as ArrayList<Habit>) })

        viewModel.viewSettings.value?.let{ settings ->
            bottomSheetSearch.setText(settings.searchByName)
            if (settings.sortByDescending != null)
                sortRadioGroup.check(if (settings.sortByDescending as Boolean) R.id.descendingSort else R.id.ascendingSort)
        }
    }

    private fun setUpSearchFilter(){
        bottomSheetSearch.doAfterTextChanged { text ->
            if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
                val newText = text?.toString()
                viewModel.viewSettings.value?.let{ settings ->
                    settings.searchByName = newText
                    viewModel.updateViewSettings(settings)
                } ?: viewModel.updateViewSettings(
                    ListViewSettings(
                        searchByName = newText
                    )
                )
                viewModel.loadHabits()
            }
        }
    }

    private fun setUpSortFilter(){
        sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
                val sortOptions = checkedId == R.id.descendingSort
                viewModel.viewSettings.value?.let { settings ->
                    settings.sortByDescending = sortOptions
                    viewModel.updateViewSettings(settings)
                } ?: viewModel.updateViewSettings(
                    ListViewSettings(
                        sortByDescending = sortOptions
                    )
                )
                viewModel.loadHabits()
            }
        }
    }

    private fun setupFilters() {
        setUpSearchFilter()
        setUpSortFilter()
    }

    private fun setupBottomSheet(state: Int){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior?.state = state
        bottomSheetBehavior?.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) = viewModel.loadHabits()
        })
    }

    private fun getViewPagerFragment(habits: ArrayList<Habit>): ViewPagerFragment?{
            return if (bottomSheetSearch.text.isNullOrEmpty()){
                viewModel.updateViewSettings(null)
                sortRadioGroup.clearCheck()
                ViewPagerFragment.newInstance(
                    habits
                )
            }
            else null
    }

    private fun addNewFragment(fragment: Fragment){
        childFragmentManager.popBackStack()
        var transaction = childFragmentManager
            .beginTransaction()
            .replace(R.id.listFragment, fragment)

        transaction = if (fragment is RecyclerViewFragment) transaction.addToBackStack(null) else transaction

        transaction.commit()
    }


    private fun setFragment(state: Int, habits: ArrayList<Habit>) =
        when(state){
            BottomSheetBehavior.STATE_COLLAPSED -> getViewPagerFragment(habits)
            BottomSheetBehavior.STATE_EXPANDED -> RecyclerViewFragment.newInstance(
                habits
            )
            else -> null
        }?.let { fragment ->  addNewFragment(fragment) }
}