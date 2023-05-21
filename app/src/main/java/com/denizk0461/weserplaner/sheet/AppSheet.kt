package com.denizk0461.weserplaner.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet super class providing common functionality. All bottom sheets should inherit from
 * this.
 *
 * @param layoutId  ID of the layout of the bottom sheet
 */
open class AppSheet(@LayoutRes private val layoutId: Int) : BottomSheetDialogFragment() {

    // Internal context object
    private lateinit var _context: Context

    /**
     * Get non-null context. Only valid after onAttach().
     */
    override fun getContext(): Context = _context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        LayoutInflater.from(context).inflate(layoutId, container, false)

    /**
     * Opens a bottom sheet. Sheet will be attached to the fragment manager of the parent of the
     * sheet that's calling this function. Sheet must be a subclass of
     * [com.denizk0461.weserplaner.sheet.AppSheet].
     *
     * @param sheet element that will be displayed
     */
    protected fun openBottomSheet(sheet: AppSheet) {
        sheet.show(parentFragmentManager, sheet.javaClass.simpleName)
    }
}