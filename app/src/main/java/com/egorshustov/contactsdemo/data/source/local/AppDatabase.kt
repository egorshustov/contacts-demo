package com.egorshustov.contactsdemo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.egorshustov.contactsdemo.data.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactsDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "App.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}