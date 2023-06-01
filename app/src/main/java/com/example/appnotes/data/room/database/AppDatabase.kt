package com.example.appnotes.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.data.room.dao.NoteDao

@Database(entities = [NoteModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao
    companion object{
        @Volatile
        private var db : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "note.db"
            )
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build().also { db = it }
        }
    }
}