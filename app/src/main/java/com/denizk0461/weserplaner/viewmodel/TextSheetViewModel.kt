package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.OfferCanteen

class TextSheetViewModel(application: Application) : AppViewModel(application) {

    /**
     * Retrieves the canteen stored in the database.
     *
     * @return canteen
     */
    fun getCanteen(): LiveData<OfferCanteen> = repo.getCanteen()

    /**
     * Retrieve opening hours for the fetched canteen.
     * Used when contentId = [com.denizk0461.weserplaner.model.TextSheetContentId.OPENING_HOURS]
     *
     * @return opening hours for the canteen
     */
    fun getCanteenOpeningHours(): LiveData<String> = repo.getCanteenOpeningHours()

    /**
     * Retrieve news for the fetched canteen.
     * Used when contentId = [com.denizk0461.weserplaner.model.TextSheetContentId.NEWS]
     *
     * @return news for the canteen
     */
    fun getCanteenNews(): LiveData<String> = repo.getCanteenNews()
}