package com.axelia.yelpprototype.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.axelia.yelpprototype.data.local.dao.BusinessDao
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.model.UtilConverters

@Database(
    entities = [Business::class],
    version = DatabaseMigrations.DB_VERSION
)
@TypeConverters(UtilConverters::class)
abstract class YelpDatabase : RoomDatabase() {

    abstract fun getBusinessDao(): BusinessDao

    companion object {
        const val DB_NAME = "yelp_database"

        @Volatile
        private var INSTANCE: YelpDatabase? = null

        fun getInstance(context: Context): YelpDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        YelpDatabase::class.java,
                        DB_NAME
                    )
                    .addMigrations(*DatabaseMigrations.MIGRATIONS)
                    .build()

                INSTANCE = instance
                return instance
            }
        }

    }
}