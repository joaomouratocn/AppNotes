package com.example.appnotes.data.repository

import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.data.room.dao.NoteDao
import com.example.appnotes.data.webclient.webclientservices.NoteWebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NoteRepository(private val noteDao:NoteDao, private val noteWebService: NoteWebService) {

    suspend fun sync(){
        noteDao.getNotesDisable().first()?.map{
            deleteWebNote(it)
        }
        noteDao.getNotesForSync().first()?.map {
            syncWebNote(it)
        }
        updateAllNotes()
    }

    private suspend fun updateAllNotes(){
        noteWebService.getAllNotes()?.let { noteModelList ->
            val syncedNotes = noteModelList.map { it.copy(synced = true) }
            noteDao.insertNote(syncedNotes)
        }
    }

    fun getAllNotes(): Flow<List<NoteModel>> {
        return noteDao.getAllNotes()
    }

    fun getNoteById(receivedNoteId: String): Flow<NoteModel?> {
        return noteDao.getNoteById(receivedNoteId)
    }

    suspend fun insertNote(noteModel: NoteModel) {
        noteDao.insertNote(noteModel)
        syncWebNote(noteModel)
    }

    suspend fun deleteNote(noteModel: NoteModel) {
        noteDao.insertNote(noteModel.copy(disable = true))
        deleteWebNote(noteModel)
    }

    private suspend fun syncWebNote(noteModel: NoteModel) {
        if (noteWebService.insertNote(noteModel)) {
            val syncedNote = noteModel.copy(synced = true)
            noteDao.insertNote(syncedNote)
        }
    }

    private suspend fun deleteWebNote(it: NoteModel) {
        if (noteWebService.deleteNote(it.id)) {
            noteDao.deleteNote(it)
        }
    }
}