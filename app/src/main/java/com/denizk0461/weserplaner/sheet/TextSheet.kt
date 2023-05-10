package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetTextBinding
import com.denizk0461.weserplaner.model.TextSheetContentId
import com.denizk0461.weserplaner.viewmodel.TextSheetViewModel

/**
 * Bottom sheet used for displaying any sort of text to the user.
 */
class TextSheet : AppSheet(R.layout.sheet_text) {

    // View binding
    private val binding: SheetTextBinding by viewBinding(SheetTextBinding::bind)

    // View model; used to retrieve certain texts as LiveData rather than receiving the raw string
    private val viewModel: TextSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve headline for the window
        val header = arguments?.getString("header") ?: ""

        /*
         * Retrieve certain ID to denote which text content LiveData should be observed; if this is
         * -1, assume that a raw string has been passed instead
         */
        val contentId = arguments?.getInt("contentId") ?: TextSheetContentId.PASS_RAW_STRING

        // Check whether an ID has passed to use LiveData
        val content = if (contentId == TextSheetContentId.PASS_RAW_STRING) {
            // Retrieve content for the window as raw string if this has been picked
            arguments?.getString("content") ?: ""
        } else "" // This empty string will be unused

        // Set whether the sheet should be cancellable by actions such as swiping
        val isSheetCancellable = arguments?.getBoolean("isCancellable") == true

        // Set text views to raw string passed if ID says so
        if (contentId == -1) {
            binding.textHeader.text = header
            binding.textContent.text = content
//        } else (when (contentId) {
//            // Observe LiveData if corresponding ID has been passed
//            TextSheetContentId.OPENING_HOURS -> viewModel.getCanteen()
//            TextSheetContentId.NEWS -> viewModel.getCanteen()
//            else -> throw InvalidContentIdException()
//        }).observe(viewLifecycleOwner) { observedContent ->
//            // Show the content
//            binding.textContent.text = (observedContent ?: "")
//                // If the content is empty, tell the user
//                .ifBlank { emptyContentStringFor(contentId) }
//        }
        } else {
            viewModel.getCanteen().observe(viewLifecycleOwner) { canteen ->
                when (contentId) {
                    TextSheetContentId.OPENING_HOURS -> {
                        binding.textHeader.text = getString(
                            R.string.text_sheet_opening_hours_title, canteen.canteen
                        )
                        binding.textContent.text = canteen.openingHours.ifBlank {
                            getString(R.string.text_sheet_opening_hours_empty)
                        }
                    }
                    TextSheetContentId.NEWS -> {
                        binding.textHeader.text = getString(
                            R.string.text_sheet_news_title, canteen.canteen
                        )
                        binding.textContent.text = canteen.news.ifBlank {
                            getString(R.string.text_sheet_news_empty)
                        }
                    }
                    else -> {} // should not occur
                }
            }
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

    /**
     * Retrieves a string to show to the user if the content is empty.
     *
     * @param contentId used to determine which string res should be used
     * @return          localised string to show the user
     */
    private fun emptyContentStringFor(contentId: Int) = getString(when (contentId) {
        TextSheetContentId.OPENING_HOURS -> R.string.text_sheet_opening_hours_empty
        TextSheetContentId.NEWS -> R.string.text_sheet_news_empty
        else -> R.string.error_generic
    })
}