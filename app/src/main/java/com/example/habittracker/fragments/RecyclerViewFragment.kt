import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.adapters.RecyclerAdapter
import com.example.habittracker.models.HabitType
import com.example.habittracker.models.viewmodels.ListHabitsViewModel
import com.example.habittracker.models.viewmodels.ListViewModelProviderFactory
import com.example.habittracker.repository.RoomHabitsProvider
import kotlinx.android.synthetic.main.recycler_view_fragment.*
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import java.util.*

class RecyclerViewFragment : Fragment() {
    private lateinit var viewModel: ListHabitsViewModel
    private var recyclerAdapter: RecyclerAdapter? = null
    private var habitType: HabitType? = null
    private var callback: RecyclerViewCallback? = null

    interface RecyclerViewCallback {
        fun onEditClickListener(habitId: UUID)
    }

    companion object{
        private const val HABIT_TYPE_ARG = "HABIT_TYPE_ARG"

        fun newInstance(habitType: HabitType) : RecyclerViewFragment {
            val args = Bundle()
            args.putString(HABIT_TYPE_ARG, habitType.name)
            val newFragment =
                RecyclerViewFragment()
            newFragment.arguments = args
            return newFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        habitType?.let{outState.putString(HABIT_TYPE_ARG, it.name)}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as RecyclerViewCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (arguments?.getString(HABIT_TYPE_ARG) ?: savedInstanceState?.getString(HABIT_TYPE_ARG))?.let{
            habitType = HabitType.valueOf(it)
        }

        activity?.let{ activity ->
            viewModel = ViewModelProvider(activity, ListViewModelProviderFactory)
                .get(ListHabitsViewModel::class.java)
        }    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycler_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habitsSource.observe(this, androidx.lifecycle.Observer { habits -> viewModel.updateFilteredHabits(habits) })
        viewModel.filteredHabits.observe(this, androidx.lifecycle.Observer { habits ->
            recyclerView.apply {
                callback?.let { callback ->
                    recyclerAdapter = RecyclerAdapter(habitType?.let{ type -> habits.filter { habit -> habit.type == type }} ?: habits)
                    layoutManager = LinearLayoutManager(activity)
                    recyclerAdapter?.setOnViewHolderClickListener { habit, _ ->
                        callback.onEditClickListener(habit.id)
                    }
                    adapter = recyclerAdapter

                    val swipeHandler = object : SwipeToDeleteCallback(context) {
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val adapter = recyclerView.adapter as RecyclerAdapter
                            val habitToDelete = adapter.getItemAt(viewHolder.adapterPosition)
                            viewModel.deleteHabit(habitToDelete.toHabitEntity())
                        }
                    }
                    val itemTouchHelper = ItemTouchHelper(swipeHandler)
                    itemTouchHelper.attachToRecyclerView(recyclerView)
                }
            }
        })
    }
}