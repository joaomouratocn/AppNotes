package com.example.appnotes.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appnotes.data.repository.NoteRepository
import com.example.appnotes.data.room.database.AppDatabase
import com.example.appnotes.data.webclient.webclientservices.NoteWebService
import com.example.appnotes.databinding.ActivityListNotesBinding
import com.example.appnotes.ui.adapter.NotesAdapter
import com.example.appnotes.util.NOTE_ID
import com.example.appnotes.util.gotoActivity
import kotlinx.coroutines.launch

class ActivityListNotes : AppCompatActivity() {
    private val binding by lazy {
        ActivityListNotesBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        NoteRepository(
            noteWebService = NoteWebService(),
            noteDao = AppDatabase.getInstance(this).noteDao()
        )
    }

    private val noteAdapter by lazy {
        NotesAdapter { noteEntity ->
            gotoActivity(ActivityNote::class.java){
                putExtra(NOTE_ID, noteEntity.id)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureSwipeRefresh()
        configureAdapter()
        configureFab()
        lifecycleScope.launch {
            launch { repository.sync() }
            repeatOnLifecycle(Lifecycle.State.STARTED){
                getAllNotes()
            }
        }
    }

    private suspend fun getAllNotes() {
        repository.getAllNotes().collect{noteList->
            if (noteList.isEmpty()){
                binding.txvEmptyList.visibility = View.VISIBLE
            }else{
                binding.txvEmptyList.visibility = View.GONE
                noteAdapter.updateNotes(noteList)
            }
        }
    }

    private fun configureSwipeRefresh(){
        binding.swipeRefresh.setOnRefreshListener {
            sync()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configureAdapter() {
        binding.apply {
            recycleNotes.adapter = noteAdapter
        }
    }

    private fun configureFab() {
        binding.fabNewNote.setOnClickListener { gotoActivity(ActivityNote::class.java) }
    }

    private fun sync(){
        lifecycleScope.launch { repository.sync() }
    }
}