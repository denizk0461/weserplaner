package com.denizk0461.studip.model

data class CanteenOfferGroup(
    val date: String,
    val dateId: Int,
    val category: String,
    val canteen: String,
    val offers: List<CanteenOfferGroupElement>,
)
