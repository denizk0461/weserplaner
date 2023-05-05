package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetTextBinding

/**
 * Bottom sheet used for displaying any sort of text to the user.
 */
class TextSheet : AppSheet(R.layout.sheet_text) {

    // View binding
    private val binding: SheetTextBinding by viewBinding(SheetTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve headline for the window
        val header = arguments?.getString("header") ?: ""

        // Retrieve content for the window
        val content = arguments?.getString("content") ?: ""

        // Set whether the sheet should be cancellable by actions such as swiping
        val isSheetCancellable = arguments?.getBoolean("isCancellable") == true

        // Set text values
        binding.apply {
            textHeader.text = header
            textContent.text = content
        }

        // Set up an appropriate close button depending on whether the sheet is cancellable
        if (isSheetCancellable) {
            // Set up a simple X button to be able to close the sheet
            binding.buttonCancel.visibility = View.VISIBLE
            binding.buttonCancel.setOnClickListener {
                // Do nothing and dismiss the sheet
                dismiss()
            }
        } else {
            // Show a more prominent button to close the sheet
            binding.buttonCancel.visibility = View.GONE
        }
    }
}