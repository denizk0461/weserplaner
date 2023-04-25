package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetTextBinding

/**
 * Bottom sheet used for displaying any sort of text to the user.
 *
 * @param header    headline for the window
 * @param content   content text for the window
 */
class TextSheet(
    private val header: String,
    private val content: String,
) : AppSheet(R.layout.sheet_text) {

    // View binding
    private val binding: SheetTextBinding by viewBinding(SheetTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set text values
        binding.apply {
            textHeader.text = header
            textContent.text = content
        }
    }
}