package com.denizk0461.weserplaner.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.denizk0461.weserplaner.model.*

/**
 * Room database that holds all objects that need to be saved to persistent storage.
 */
@Database(
    entities = [
        StudIPEvent::class,
        OfferDate::class,
        OfferCanteen::class,
        OfferCategory::class,
        OfferItem::class,
        EventTask::class,
    ],
    version = 21,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 20, to = 21),
    ],
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * The database access object. Database transactions go through this.
     */
    abstract fun dao(): AppDAO

    companion object {
        // The singular instance of this database
        private var instance: AppDatabase? = null

        /**
         * Retrieve the instance of the database.
         *
         * @param context   used to resolve the object
         * @return          the database
         */
        fun getInstance(context: Context): AppDatabase {
            /*
             * Check if an object has already been instantiated. Only create a new instance if none
             * exists.
             */
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "event_db",
                        ).addMigrations(
                            migrationAddNewsToOfferCanteen_19_20,
//                            migrationAddTimetableId_20_21,
//                            migrationAddEventTask_20_21,
                        )
//                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            // Instance can never be null at this point
            return instance!!
        }

        // - migrations - //

        /**
         * Adds the column 'news' to the data object [OfferCanteen].
         */
        private val migrationAddNewsToOfferCanteen_19_20 = object : Migration(19, 20) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE offer_canteen ADD COLUMN news TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}