package com.denizk0461.weserplaner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.weserplaner.BuildConfig
import com.denizk0461.weserplaner.data.AppDiffUtilCallback
import com.denizk0461.weserplaner.databinding.ItemTaskBinding
import com.denizk0461.weserplaner.model.EventTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskOverviewAdapter(
    private val onClickListener: OnClickListener,
) : RecyclerView.Adapter<TaskOverviewAdapter.TaskViewHolder>() {

    /**
     * List of all events.
     */
    private val tasks: MutableList<EventTask> = mutableListOf()

    /**
     * View holder class for parent class
     *
     * @param binding   view binding object
     */
    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Set the amount of items to the size of the filtered list, so only events of the current day
    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        // Retrieve item for current position
        val currentItem = tasks[position]

        // Context for easier reference
        val context = holder.binding.root.context

        // view set-up
        SimpleDateFormat("yyyy-MM-dd, HH:mm:ss.SSS", Locale.GERMANY)
            .format(Date(BuildConfig.BUILD_TIME_MILLIS))

        // Set up single click listener
        holder.binding.linearLayout.setOnClickListener {
            onClickListener.onClick(currentItem)
        }

        // Set up long press listener
        holder.binding.linearLayout.setOnLongClickListener {
            onClickListener.onLongClick(currentItem)
        }
    }

    /**
     * Updates the data and calculates the difference between the old dataset and the newly provided
     * dataset.
     *
     * @param newData   new dataset to be displayed
     */
    fun setNewData(newData: List<EventTask>) {
        // Calculate the difference between the old list and the new list
        val diffResult = DiffUtil.calculateDiff(AppDiffUtilCallback(tasks, newData))

        // Remove all items from the list
        tasks.clear()

        // Add all items from the new list
        tasks.addAll(newData)

        // Tell the DiffUtil which items have changed between the two lists
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Interface used to evaluate clicks and long presses on a given item.
     */
    interface OnClickListener {

        /**
         * Executed when an item has been clicked.
         *
         * @param task  item that has been clicked
         */
        fun onClick(task: EventTask)


        /**
         * Executed when an item has been long-pressed.
         *
         * @param task  item that has been long-pressed
         * @return      whether the long press was successful
         */
        fun onLongClick(task: EventTask): Boolean
    }
}