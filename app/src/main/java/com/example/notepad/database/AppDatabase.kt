package com.example.notepad.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notepad.database.DAO.CategoryDao
import com.example.notepad.database.DAO.NoteDao
import com.example.notepad.models.Category
import com.example.notepad.models.Note

@Database(entities = [Note::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCategoryDao() : CategoryDao
    abstract fun getNoteDao() : NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notepad_database"
                ).build()
                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
}
