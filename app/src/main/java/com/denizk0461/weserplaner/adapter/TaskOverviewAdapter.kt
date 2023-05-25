package com.denizk0461.weserplaner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.AppDiffUtilCallback
import com.denizk0461.weserplaner.data.isInThePast
import com.denizk0461.weserplaner.databinding.ItemTaskBinding
import com.denizk0461.weserplaner.model.EventTaskExtended
import com.denizk0461.weserplaner.model.FormattedDate

class TaskOverviewAdapter(
    private val onClickListener: OnClickListener,
) : RecyclerView.Adapter<TaskOverviewAdapter.TaskViewHolder>() {

    /**
     * List of all events.
     */
    private val tasks: MutableList<EventTaskExtended> = mutableListOf()

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

        // Determine whether this item's view is expanded
        var isExpanded = false

        holder.binding.layoutExpandedContainer.visibility = View.GONE

        // Context for easier reference
        val context = holder.binding.root.context

        // view set-up

        // Set event title text
        holder.binding.textEventTitle.text = currentItem.eventTitle

        // Set task title text
        holder.binding.textTitle.text = currentItem.taskTitle

        val dueDate = FormattedDate(currentItem.dueDate)

        // Set due date text to formatted date, TODO let user pick the date format
        holder.binding.textDueDate.text = dueDate.localisedString(context)

        if (currentItem.notifyDate == -1L) {

            holder.binding.layoutDueDate.visibility = View.GONE

            holder.binding.iconNotifyHeader.setImageResource(0)
        } else {
            val notificationDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.notifications,
            )

            holder.binding.layoutDueDate.visibility = View.VISIBLE

            holder.binding.iconNotifyHeader.setImageDrawable(notificationDrawable)

            holder.binding.iconNotifyDate.setImageDrawable(notificationDrawable)

            // TODO let user pick date format
            val notifyDate = FormattedDate(currentItem.notifyDate)

            holder.binding.textNotifyDate.text = context.getString(if (notifyDate.date.isInThePast()) {
                R.string.task_item_notification_active_past
            } else {
                R.string.task_item_notification_active_future
            }, notifyDate.dateString, notifyDate.timeString)
        }

        holder.binding.textLecturer.text = currentItem.lecturer
        holder.binding.textRoom.text = currentItem.room
        holder.binding.textNotes.text = currentItem.notes

        // Set up single click listener
        holder.binding.linearLayout.setOnClickListener {
            // Prepare layout animation
            TransitionManager.beginDelayedTransition(holder.binding.root.parent as ViewGroup)

            // Set visibility of further items
            holder.binding.layoutExpandedContainer.visibility = if (isExpanded) {
                View.GONE
            } else {
                View.VISIBLE
            }

            // Set value of isExpanded depending on whether the view is expanded
            isExpanded = !isExpanded
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
    fun setNewData(newData: List<EventTaskExtended>) {
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
         * Executed when an item has been long-pressed.
         *
         * @param task  item that has been long-pressed
         * @return      whether the long press was successful
         */
        fun onLongClick(task: EventTaskExtended): Boolean
    }
}