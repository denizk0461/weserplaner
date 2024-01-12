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
        Timetable::class,
        StudIPEvent::class,
        OfferDate::class,
        OfferCanteen::class,
        OfferCategory::class,
        OfferItem::class,
        EventTask::class,
    ],
    version = 23,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 20, to = 21),
//        AutoMigration(from = 21, to = 22),
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
                            migration_21_22,
                            migration_22_23,
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

        private val migration_21_22 = object : Migration(21, 22) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add 'timetables' table
                database.execSQL("CREATE TABLE 'timetables' ('id' INTEGER NOT NULL UNIQUE, 'name' TEXT NOT NULL, PRIMARY KEY('id' AUTOINCREMENT));")

                // Insert a timetable
                database.execSQL("INSERT INTO timetables VALUES (0, 'default')")

                /*
                 * Create temporary Stud.IP event table. This is necessary because SQLite doesn't
                 * implement ADD FOREIGN KEY on ALTER TABLE >:(
                 */
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS studip_events_temp (
                        'eventId' INTEGER NOT NULL UNIQUE,
                        'timetableId' INTEGER NOT NULL,
                        'title' TEXT NOT NULL,
                        'lecturer' TEXT NOT NULL,
                        'room' TEXT NOT NULL,
                        'day' INTEGER NOT NULL,
                        'timeslotStart' TEXT NOT NULL,
                        'timeslotEnd' TEXT NOT NULL,
                        'timeslotId' INTEGER NOT NULL,
                        'colour' INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY('timetableId') REFERENCES 'timetables'('id') ON DELETE CASCADE,
                        PRIMARY KEY('eventId' AUTOINCREMENT)
                    );
                """.trimIndent())

                // Copy data from old table to new table
                database.execSQL("""
                    INSERT INTO studip_events_temp (eventId, timetableId, title, lecturer, room, day, timeslotStart, timeslotEnd, timeslotId, colour)
                    SELECT eventId, timetableId, title, lecturer, room, day, timeslotStart, timeslotEnd, timeslotId, colour FROM studip_events
                """.trimIndent())

                // Delete old table
                database.execSQL("DROP TABLE studip_events")

                // Rename new table to the old table's name
                database.execSQL("ALTER TABLE studip_events_temp RENAME TO studip_events")
            }
        }

        private val migration_22_23 = object : Migration(22, 23) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE offer_date ADD COLUMN day TEXT NOT NULL DEFAULT '?'
                """)
            }
        }
    }
}