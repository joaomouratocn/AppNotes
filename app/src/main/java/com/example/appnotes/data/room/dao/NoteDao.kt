package com.example.appnotes.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appnotes.data.model.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: NoteModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModels: List<NoteModel>)

    @Delete
    suspend fun deleteNote(noteModel: NoteModel)

    @Query("SELECT * FROM NoteModel WHERE disable = 0")
    fun getAllNotes():Flow<List<NoteModel>>

    @Query("SELECT * FROM NoteModel WHERE id = :receivedNoteId AND disable = 0")
    fun getNoteById(receivedNoteId: String):Flow<NoteModel?>

    @Query("SELECT * FROM NoteModel WHERE synced = 0 AND disable = 0")
    fun getNotesForSync():Flow<List<NoteModel>?>

    @Query("SELECT * FROM NoteModel WHERE disable = 1")
    fun getNotesDisable(): Flow<List<NoteModel>?>

}