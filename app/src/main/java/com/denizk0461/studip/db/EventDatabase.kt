package com.denizk0461.studip.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.denizk0461.studip.model.*

@Database(
    entities = [
        StudIPEvent::class,
        OfferDate::class,
        OfferCanteen::class,
        OfferCategory::class,
        OfferItem::class
    ],
    version = 10,
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun dao(): EventDAO

    companion object {
        private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            if (instance == null) {
                synchronized(EventDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java,
                        "event_db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance!!
        }
    }
}