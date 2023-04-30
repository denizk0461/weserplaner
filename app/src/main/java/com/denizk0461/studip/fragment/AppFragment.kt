package com.denizk0461.studip.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.denizk0461.studip.sheet.AppSheet

/**
 * Fragment super class providing common functionality. Fragments are used for providing the user
 * with different views and functionality. All fragments should inherit from this.
 */
open class AppFragment : Fragment() {

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

    /**
     * Opens a bottom sheet. Sheet must be a subclass of [com.denizk0461.studip.sheet.AppSheet].
     *
     * @param sheet element that will be displayed
     */
    protected fun openBottomSheet(sheet: AppSheet) {
        sheet.show((context as FragmentActivity).supportFragmentManager, sheet.javaClass.simpleName)
    }
}