package com.example.appnotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class NoteModel(
    @PrimaryKey
    val id:String = UUID.randomUUID().toString(),
    val titulo: String?,
    val descricao: String?,
    val imagem: String? = null,
    val synced:Boolean = false,
    val disable:Boolean = false
)