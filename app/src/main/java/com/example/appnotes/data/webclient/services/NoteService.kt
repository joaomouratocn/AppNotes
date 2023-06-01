package com.example.appnotes.data.webclient.services

import com.example.appnotes.data.model.NoteModel
import com.example.appnotes.data.webclient.model.NoteModelBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteService {
    @GET("notas")
    suspend fun getAllNotes():Response<List<NoteModel>>
    @PUT("notas/{noteId}")

    suspend fun insertNote(
        @Path("noteId") noteId: String,
        @Body noteModelBody: NoteModelBody):Response<NoteModel>

    @DELETE("notas/{id}")
    suspend fun deleteNote(@Path("id") id: String):Response<Void>
}