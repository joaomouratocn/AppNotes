package com.example.appnotes.data.webclient.retrofit

import com.example.appnotes.data.webclient.services.NoteService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class InitRetrofit {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.235:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val noteService: NoteService = retrofit.create(NoteService::class.java)
}