package com.denizk0461.weserplaner.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.denizk0461.weserplaner.R

/**
 * Class used to mitigate issues with the ArrayAdapter deleting items.
 *
 * @param context   context reference
 * @param items     list of texts to display
 */
class DropdownAdapter(context: Context, items: MutableList<String>)
    : ArrayAdapter<String>(context, R.layout.item_dropdown, items) {

    private val noOpFilter = object : Filter() {
        private val noOpResult = FilterResults()
        override fun performFiltering(constraint: CharSequence?) = noOpResult
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
    }

    override fun getFilter() = noOpFilter
}