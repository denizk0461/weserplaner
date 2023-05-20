package com.denizk0461.weserplaner.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.denizk0461.weserplaner.sheet.AppSheet

/**
 * Fragment super class providing common functionality. Fragments are used for providing the user
 * with different views and functionality. All fragments should inherit from this.
 */
abstract class AppFragment<T : ViewBinding> : Fragment() {

    // Internal context object
    private lateinit var _context: Context

    // Nullable view binding reference. Instantiate this in classes that extend this class.
    protected var _binding: T? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    protected val binding: T get() = _binding!!

    /**
     * Get non-null context. Only valid after onAttach().
     */
    override fun getContext(): Context = _context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    /**
     * Opens a bottom sheet. Sheet must be a subclass of [com.denizk0461.weserplaner.sheet.AppSheet].
     *
     * @param sheet element that will be displayed
     */
    protected fun openBottomSheet(sheet: AppSheet) {
        sheet.show(childFragmentManager, sheet.javaClass.simpleName)
    }
}