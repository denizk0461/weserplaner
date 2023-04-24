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
    @StringRes private val header: Int,
    @StringRes private val content: Int
) : AppSheet(R.layout.sheet_text) {

    private val binding: SheetTextBinding by viewBinding(SheetTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            textHeader.text = getString(header)
            textContent.text = getString(content)
        }
    }
}