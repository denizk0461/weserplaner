package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.data.StwParser
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPrefObject
import com.denizk0461.studip.model.DietaryPreferences

class CanteenViewModel(app: Application) : TemplateViewModel(app) {

    private val parser = StwParser()

    val allOffers: LiveData<List<CanteenOffer>> = repo.allOffers

    fun getDietaryPrefs(): DietaryPrefObject = repo.getDietaryPrefsAsObj()

    fun fetchOffers(onRefreshUpdate: (status: Int) -> Unit, onFinish: () -> Unit) { doAsync { parser.parse(onRefreshUpdate, onFinish) }}

    fun setPreference(pref: DietaryPreferences, newValue: Boolean) { repo.setPreference(pref, newValue) }
    fun getPreference(pref: DietaryPreferences): Boolean = repo.getPreference(pref)
}