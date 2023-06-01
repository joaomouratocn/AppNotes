package com.example.appnotes.data.webclient.webclientservices

import android.util.Log
import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.data.webclient.model.NoteModelBody
import com.example.appnotes.data.webclient.retrofit.InitRetrofit
import com.example.appnotes.data.webclient.services.NoteService
import retrofit2.Response

class NoteWebService {
    private val retrofitNoteService: NoteService = InitRetrofit().noteService

    suspend fun getAllNotes():List<NoteModel>?{
        return try {
            val response = retrofitNoteService.getAllNotes()
            response.body()?.map {
                it
            } ?: emptyList()
        } catch (e: Exception) {
            Log.i("TAG", "getAllNotes: $e")
            null
        }
    }

    suspend fun insertNote(noteModel: NoteModel): Boolean {
        try {
            val insertNote: Response<NoteModel> = retrofitNoteService.insertNote(
                    noteId = noteModel.id,
                    noteModelBody = NoteModelBody(
                        titulo = noteModel.titulo,
                        descricao = noteModel.descricao,
                        imagem = noteModel.imagem
                    )
                )
            return insertNote.isSuccessful
        } catch (e: Exception) {
            Log.i("TAG", "insertNote: $e")
        }
        return false
    }

    suspend fun deleteNote(id: String): Boolean {
        try {
            val deleteNote = retrofitNoteService.deleteNote(id)
            return true
        } catch (e: Exception) {
            Log.i("TAG", "deleteNote: $e")
        }
        return false
    }
}