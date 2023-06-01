package com.example.appnotes.data.webclient.model

data class NoteModelBody(
    val titulo: String?,
    val descricao: String?,
    val imagem: String? = null
)