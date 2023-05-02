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

        // Set text values
        binding.apply {
            textHeader.text = header
            textContent.text = content
        }
    }
}