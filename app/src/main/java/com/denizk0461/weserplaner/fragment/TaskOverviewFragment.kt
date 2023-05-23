package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.adapter.TaskOverviewAdapter
import com.denizk0461.weserplaner.data.showSnackBar
import com.denizk0461.weserplaner.databinding.FragmentTaskOverviewBinding
import com.denizk0461.weserplaner.model.EventTask
import com.denizk0461.weserplaner.values.TaskOrder
import com.denizk0461.weserplaner.viewmodel.TaskOverviewViewModel

/**
 * Fragment that shows the user an overview of their tasks and exams.
 */
class TaskOverviewFragment : AppFragment<FragmentTaskOverviewBinding>(),
    TaskOverviewAdapter.OnClickListener {

    // View model reference for providing access to the database
    private val viewModel: TaskOverviewViewModel by viewModels()

    // Task adapter for the recycler view
    private val taskAdapter: TaskOverviewAdapter = TaskOverviewAdapter(this)

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the title to be selected so it scrolls (marquee)
        binding.appTitleBar.isSelected = true

        // Tell the user that this fragment's functionality is not yet implemented
        context.theme.showSnackBar(
            binding.coordinatorLayout,
            getString(R.string.task_overview_snack_unimplemented),
        )

        binding.buttonOrder.setOnClickListener {
            // Create menu for selecting a new canteen
            PopupMenu(binding.root.context, binding.buttonOrder).apply {
                setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        R.id.title_alphabetically -> {}
                        R.id.date_created -> {}
                        R.id.date_due -> {}
                        else -> {} // repeat first option
                    }

                    // TODO set user's ordering preference

                    // TODO refresh the view
                    true
                }
                inflate(R.menu.menu_task_sort)
                show()
            }
        }

        binding.recyclerViewLayout.recyclerView.apply {
            // Set page to scroll vertically
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            // Assign adapter to this recycler view
            adapter = taskAdapter
        }

        // insert sample data TODO change to db
        taskAdapter.setNewData(viewModel.getTasks(TaskOrder.DATE_DUE))

        // Set up scroll change listener to shrink and extend FAB accordingly
        binding.recyclerViewLayout.recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // Calculate the vertical scroll difference
            val differenceY = scrollY - oldScrollY
            if (differenceY > 0) {
                // If scrolling down, shrink the FAB
                binding.fabAddTask.shrink()
            } else if (differenceY < 0) {
                // If scrolling up, extend the FAB
                binding.fabAddTask.extend()
            }
        }
    }

    override fun onClick(task: EventTask) {
        // TODO
    }

    override fun onLongClick(task: EventTask): Boolean {
        // TODO
        return false
    }
}