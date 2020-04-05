package com.example.habittracker.fragments

import RecyclerViewFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.*
import com.example.habittracker.models.Habit
import com.example.habittracker.models.ListViewSettings
import com.example.habittracker.models.viewmodels.ListHabitsViewModel
import com.example.habittracker.models.viewmodels.ListViewModelProviderFactory
import com.example.habittracker.repository.RoomHabitsProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(){
    private var callback: MainFragmentCallback? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var viewModel: ListHabitsViewModel

    companion object{
        private const val BOTTOM_SHEET_STATE = "BOTTOM_SHEET_STATE"
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
        activity?.let{ activity ->
            viewModel = ViewModelProvider(activity, ListViewModelProviderFactory)
                .get(ListHabitsViewModel::class.java)
        }
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
        setFragment(bottomSheetBehavior?.state ?: BottomSheetBehavior.STATE_COLLAPSED)
    }

    private fun setUpViewModel(){
        viewModel.viewSettings.observe(this, Observer { viewSettings -> viewSettings?.let{ settings ->
            shouldChange(settings.searchByName, bottomSheetSearch)?.let{ newField -> bottomSheetSearch.setText(newField)}
            settings.sortByDescending?.let{sortByDescending ->
                if (sortByDescending != isRadioButtonDescending(sortRadioGroup.checkedRadioButtonId))
                    sortRadioGroup.check(isDescendingToRadioButton(sortByDescending))
            } }
        })
    }

    private fun isRadioButtonDescending(radioButtonId: Int): Boolean = radioButtonId == descendingSort.id

    private fun isDescendingToRadioButton(sortByDescending: Boolean): Int =
        if (sortByDescending) R.id.descendingSort else R.id.ascendingSort

    private fun shouldChange(newField: String?, editText: EditText): String? =
        if (newField != null && newField != editText.text.toString()) newField else null

    private fun setUpSearchFilter(){
        bottomSheetSearch.doAfterTextChanged { text ->
            if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
                val newText = text?.toString()
                viewModel.updateViewSettings { settings -> settings?.let{ it.searchByName = newText }}
            }
        }
    }

    private fun setUpSortFilter(){
        sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_COLLAPSED) {
                val sortByDescending = isRadioButtonDescending(checkedId)
                viewModel.updateViewSettings { settings -> settings?.let{ it.sortByDescending = sortByDescending }}
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

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                setFragment(bottomSheetBehavior?.state ?: BottomSheetBehavior.STATE_COLLAPSED)
            }
        })
    }

    private fun getViewPagerFragment(): ViewPagerFragment?{
            return if (bottomSheetSearch.text.isNullOrEmpty()){
                viewModel.clearViewSettings()
                sortRadioGroup.clearCheck()
                ViewPagerFragment()
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


    private fun setFragment(state: Int) =
        when(state){
            BottomSheetBehavior.STATE_COLLAPSED -> getViewPagerFragment()
            BottomSheetBehavior.STATE_EXPANDED -> RecyclerViewFragment()
            else -> null
        }?.let { fragment ->  addNewFragment(fragment) }
}