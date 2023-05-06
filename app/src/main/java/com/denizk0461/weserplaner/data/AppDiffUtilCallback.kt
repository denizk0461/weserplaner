package com.denizk0461.weserplaner.data

import androidx.recyclerview.widget.DiffUtil

/**
 * Utility class used to calculate the difference between two lists to provide the adapter with
 * means to perform operations only on items that have been newly added/modified/deleted.
 *
 * @param oldList   old list
 * @param newList   new list
 */
class AppDiffUtilCallback(
    private val oldList: List<Any>,
    private val newList: List<Any>,
) : DiffUtil.Callback() {

    /**
     * Get the old list's size count.
     *
     * @return  size of the old list
     */
    override fun getOldListSize(): Int = oldList.size

    /**
     * Get the new list's size count.
     *
     * @return  size of the new list
     */
    override fun getNewListSize(): Int = newList.size

    /**
     * Check whether two items in the lists contain items of the same class.
     *
     * @param oldItemPosition   position of the item in the old list to be checked
     * @param newItemPosition   position of the item in the new list to be checked
     * @return                  whether the two items are of the same class
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].javaClass == newList[newItemPosition].javaClass

    /**
     * Check whether two items in the lists have the same contents.
     *
     * @param oldItemPosition   position of the item in the old list to be checked
     * @param newItemPosition   position of the item in the new list to be checked
     * @return                  whether the two items are the same (content is equal)
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
}