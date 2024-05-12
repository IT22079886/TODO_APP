package com.codility.todoapp.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Todo::class], version = 2) // Update version to 2
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    )
                        .addMigrations(MIGRATION_1_2) // Add migration from version 1 to 2
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

        // Migration from version 1 to 2
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add a new column 'priorityLevel' to the existing 'todoTable'
                database.execSQL("ALTER TABLE todoTable ADD COLUMN priorityLevel TEXT NOT NULL DEFAULT 'Level One'")
            }
        }
    }
}
