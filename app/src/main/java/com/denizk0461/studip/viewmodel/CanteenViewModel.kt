package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.data.StwParser
import com.denizk0461.studip.model.CanteenOffer

class CanteenViewModel(app: Application) : TemplateViewModel(app) {

    private val parser = StwParser()

    val allOffers: LiveData<List<CanteenOffer>> = repo.allOffers

    fun insertOffers(offers: List<CanteenOffer>) { doAsync { repo.insertOffers(offers) }}
    fun nukeOffers() { doAsync { repo.nukeOffers() }}

    fun fetchOffers(onFinish: () -> Unit) { doAsync { parser.parse(onFinish) }}
}