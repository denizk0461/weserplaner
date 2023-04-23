package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetAllergenBinding
import com.denizk0461.studip.model.CanteenOfferGroupElement

class AllergenSheet(private val offer: CanteenOfferGroupElement) : AppSheet(R.layout.sheet_allergen) {

    private val binding: SheetAllergenBinding by viewBinding(SheetAllergenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            title.text = offer.title
            textContent.text = "hello there"
        }
    }
}